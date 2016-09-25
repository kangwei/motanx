package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;

import java.lang.reflect.Method;

/**
 * 默认的服务提供者，SPI
 * Created by kangwei on 2016/8/24.
 */
@Spi(name = "motanx")
public class DefaultProvider<T> extends AbstractProvider<T> {

    public DefaultProvider(Class<T> interfaceClass, URL url, T impl) {
        super(url, interfaceClass, impl);
    }

    public Response doInvoke(Method method, Request request) throws Exception {
        DefaultResponse response = new DefaultResponse();
        Object invoke = method.invoke(impl, request.getArgs());
        response.setValue(invoke);
        return response;
    }

    @Override
    protected boolean doDestroy() {
        serviceMethods.clear();
        return true;
    }
}
