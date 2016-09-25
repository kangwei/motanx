package com.opensoft.motanx.rpc.cluster;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;

import java.util.List;

/**
 * 高可用策略
 * Created by kangwei on 2016/9/4.
 */
@Spi
public interface HaStrategy {
    <T> Response call(Request request, LoadBalance loadbalance, List<Provider<T>> providers);
}
