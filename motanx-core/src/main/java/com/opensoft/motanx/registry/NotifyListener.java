package com.opensoft.motanx.registry;

import com.opensoft.motanx.core.URL;

import java.util.List;

/**
 * 订阅注册中心回调函数
 * Created by kangwei on 2016/9/2.
 */
public interface NotifyListener {
    /**
     * 服务注册变化时回调处理
     *
     * @param registryUrl 注册中心地址
     * @param urls        服务地址列表
     */
    void notify(URL registryUrl, List<URL> urls);
}
