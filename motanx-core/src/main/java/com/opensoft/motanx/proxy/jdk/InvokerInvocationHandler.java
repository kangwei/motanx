package com.opensoft.motanx.proxy.jdk;

import com.opensoft.motanx.rpc.Invoker;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.RpcContext;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import com.opensoft.motanx.utils.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by kangwei on 2016/9/9.
 */
public class InvokerInvocationHandler implements InvocationHandler {
    private Invoker invoker;
    private Class cls;

    public InvokerInvocationHandler(Class cls, Invoker invoker) {
        this.cls = cls;
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        boolean isMethodInheritFromObject = "toString".equals(methodName)
                || "equals".equals(methodName)
                || "hashCode".equals(methodName);
        if (isMethodInheritFromObject) {
            return method.invoke(proxy, args);
        }
        DefaultRequest request = new DefaultRequest();
        request.setMethodName(methodName);
        request.setInterfaceName(cls.getName());
        request.setRequestId(UUID.randomUUID().toString());
        request.setArgs(args);
        request.setParameterTypes(method.getParameterTypes());
        RpcContext.getContext().setStartTime(System.nanoTime());
        Response response = invoker.invoke(request);
        return response.getValue();
    }
}
