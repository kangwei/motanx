package com.opensoft.motanx.rpc.cluster;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.rpc.Provider;

import java.util.List;

/**
 * 负载均衡
 * Created by kangwei on 2016/9/4.
 */
@Spi
public interface LoadBalance {

    <T> Provider<T> select(List<Provider<T>> providers);
}
