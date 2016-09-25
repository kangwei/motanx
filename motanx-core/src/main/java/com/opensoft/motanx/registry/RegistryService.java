package com.opensoft.motanx.registry;

import com.opensoft.motanx.core.URL;

import java.util.List;

/**
 * 注册中心服务
 * Created by kangwei on 2016/9/2.
 */
public interface RegistryService {
    /**
     * 注册服务
     *
     * @param url
     */
    void register(URL url);

    /**
     * 取消服务
     *
     * @param url
     */
    void unRegister(URL url);

    /**
     * 订阅某个服务
     *
     * @param url
     * @param listener
     */
    void subscribe(URL url, NotifyListener listener);

    /**
     * 取消订阅
     *
     * @param url
     * @param listener
     */
    void unsubscribe(URL url, NotifyListener listener);

    /**
     * 查询服务列表
     *
     * @param url
     * @return
     */
    List<URL> lookup(URL url);
}
