package com.opensoft.motanx.rpc.support;

import com.google.common.collect.Maps;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.exception.ErrorCode;
import com.opensoft.motanx.exception.MotanxException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.RpcContext;
import com.opensoft.motanx.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * Provider抽象实现
 * Created by kangwei on 2016/8/24.
 */
public abstract class AbstractProvider<T> extends AbstractNode implements Provider<T> {
    public static final Logger logger = LoggerFactory.getLogger(AbstractProvider.class);

    protected Class<T> interfaceClass;
    protected T impl;

    /**
     * 保存服务方法列表，用于call服务时快速定位方法
     */
    protected ConcurrentMap<String, Method> serviceMethods = Maps.newConcurrentMap();

    public AbstractProvider(URL url, Class<T> interfaceClass, T impl) {
        super(url);
        this.interfaceClass = interfaceClass;
        this.impl = impl;
        initServiceMethod();
    }

    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public T getImpl() {
        return impl;
    }

    /**
     * 初始化service方法
     */
    private void initServiceMethod() {
        Method[] methods = interfaceClass.getDeclaredMethods();
        for (Method method : methods) {
            serviceMethods.putIfAbsent(ReflectUtils.getMethodDesc(method), method);
        }
    }

    /**
     * lookup服务方法
     *
     * @param request 请求
     * @return 服务方法
     * @throws MotanxException 未找到时抛出not found异常
     */
    protected Method lookup(Request request) throws MotanxException {
        String parametersDesc = ReflectUtils.getParameterDesc(request.getParameterTypes());
        String methodName = request.getMethodName();
        String methodDesc = ReflectUtils.getMethodDesc(methodName, parametersDesc);
        Method method = serviceMethods.get(methodDesc);
        if (method == null) {
            throw new MotanxException(ErrorCode.illegalArgument.getCode(), "method " + methodDesc + " is not found");
        }
        return method;
    }

    public String desc() {
        return url.toString();
    }

    public URL getUrl() {
        return url;
    }

    public Response invoke(Request request) {
        long startTime = RpcContext.getContext().getStartTime();
        try {
            Method method = lookup(request);
            Response response = doInvoke(method, request);
            setProcessTime(response, System.currentTimeMillis() - startTime);
            return response;
        } catch (Exception e) {
            //TODO:日志需要记录调用方的IP和端口
            logger.error(e.getMessage(), e);
            return buildErrorResponse(startTime, e);
        } catch (Throwable e) {
            //如果服务出现Error，将其转换为Exception，防止拖垮调用方
            return buildErrorResponse(startTime, new MotanxException(e));
        }
    }

    private Response buildErrorResponse(long startTime, Exception e) {
        DefaultResponse response = new DefaultResponse();
        response.setException(e);
        response.setProcessTime(System.currentTimeMillis() - startTime);
        return response;
    }

    private void setProcessTime(Response response, long processTime) {
        if (response instanceof DefaultResponse) {
            ((DefaultResponse) response).setProcessTime(processTime);
        }
    }

    @Override
    protected boolean doInit() {
        return true;
    }

    @Override
    protected boolean doDestroy() {
        return true;
    }

    /**
     * 模板方法
     *
     * @param method  请求方法
     * @param request 请求
     * @return 响应
     */
    public abstract Response doInvoke(Method method, Request request) throws Exception;
}
