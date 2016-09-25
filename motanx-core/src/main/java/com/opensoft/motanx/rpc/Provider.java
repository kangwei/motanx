package com.opensoft.motanx.rpc;

/**
 * 服务提供者
 * Created by kangwei on 2016/8/24.
 */
public interface Provider<T> extends Invoker<T>, Node {
    /**
     * 获取服务接口
     *
     * @return 服务接口
     */
    Class<T> getInterface();

    T getImpl();
}
