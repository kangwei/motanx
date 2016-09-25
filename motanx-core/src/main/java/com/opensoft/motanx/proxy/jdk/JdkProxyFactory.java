package com.opensoft.motanx.proxy.jdk;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.proxy.support.AbstractProxyFactory;
import com.opensoft.motanx.rpc.Invoker;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.support.DefaultProvider;

import java.lang.reflect.Proxy;

/**
 * Created by kangwei on 2016/8/28.
 */
@Spi(name = "jdk")
public class JdkProxyFactory extends AbstractProxyFactory implements ProxyFactory {
    @Override
    protected <T> T doGetProxy(Class<T> cls, Invoker<T> invoker) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{cls}, new InvokerInvocationHandler(cls, invoker));
    }

    @Override
    protected <T> Provider<T> doGetReferer(final T impl, final Class<T> cls, URL url) {
        return new DefaultProvider<T>(cls, url, impl);
    }
}
