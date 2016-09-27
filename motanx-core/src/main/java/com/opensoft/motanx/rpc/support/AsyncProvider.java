package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.core.NamedThreadFactory;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kangwei on 2016/9/27.
 */
public class AsyncProvider<T> extends AbstractProvider<T> {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(32, new NamedThreadFactory("async", true));
    public AsyncProvider(Class<T> interfaceClass, URL url, T impl) {
        super(url, interfaceClass, impl);
    }

    @Override
    public Response doInvoke(Method method, Request request) throws Exception {
        FutureResponse response = new FutureResponse();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Object invoke = method.invoke(impl, request.getArgs());
                    response.onSuccess(invoke);
                } catch (Exception e) {
                    response.onFailure(e);
                }
            }
        });

        return response;
    }
}
