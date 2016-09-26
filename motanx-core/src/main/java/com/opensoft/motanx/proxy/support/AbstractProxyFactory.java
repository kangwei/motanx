package com.opensoft.motanx.proxy.support;

import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.rpc.Invoker;

/**
 * Created by kangwei on 2016/8/28.
 */
public abstract class AbstractProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Class<T> cls, Invoker<T> invoker) {
        if (invoker == null) {
            return null;
        }

        return doGetProxy(cls, invoker);
    }

    protected abstract <T> T doGetProxy(Class<T> cls, Invoker<T> invoker);
}
