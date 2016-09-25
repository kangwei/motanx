package com.opensoft.motanx.proxy.cglib;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.proxy.support.AbstractProxyFactory;
import com.opensoft.motanx.rpc.Invoker;

/**
 * Created by kangwei on 2016/9/25.
 */
@Spi(name = "cglib")
public class CglibProxyFacotory extends AbstractProxyFactory implements ProxyFactory {
    @Override
    protected <T> T doGetProxy(Class<T> cls, Invoker<T> invoker) {
        return null;
    }
}
