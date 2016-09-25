package com.opensoft.motanx.rpc.cluster;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.rpc.Invoker;
import com.opensoft.motanx.rpc.Provider;

import java.util.List;

/**
 * 集群
 * Created by kangwei on 2016/9/4.
 */
@Spi
public interface Cluster<T> extends Invoker<T> {
    /**
     * 获取服务referer列表
     *
     * @return
     */
    List<Provider<T>> getReferers();

    /**
     * 设置高可用策略
     *
     * @param haStrategy
     */
    void setHaStrategy(HaStrategy haStrategy);

    /**
     * 设置负载均衡
     *
     * @param loadBalance
     */
    void setLoadBalance(LoadBalance loadBalance);

    /**
     * 刷新referer
     * @param referers
     */
    void onRefresh(List<Provider<T>> referers);

}
