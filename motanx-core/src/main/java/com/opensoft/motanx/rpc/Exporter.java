package com.opensoft.motanx.rpc;

/**
 * 暴露服务
 * Created by kangwei on 2016/8/24.
 */
public interface Exporter<T> {
    /**
     * 暴露服务，获取服务提供者
     *
     * @return 服务提供者
     */
    Provider<T> getProvider();

    /**
     * 取消服务暴露
     */
    void unexport();
}
