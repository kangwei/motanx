package com.opensoft.motanx.proxy;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.rpc.Invoker;
import com.opensoft.motanx.rpc.Provider;

/**
 * 代理工厂，获取服务的服务实例和referer的代理
 * Created by kangwei on 2016/8/28.
 */
@Spi
public interface ProxyFactory {
    <T> T getProxy(Class<T> cls, Invoker<T> invoker);

    <T> Provider<T> getReferer(T impl, Class<T> cls, URL url);
}