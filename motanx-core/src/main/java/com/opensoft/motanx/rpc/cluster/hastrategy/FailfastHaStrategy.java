package com.opensoft.motanx.rpc.cluster.hastrategy;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.cluster.HaStrategy;
import com.opensoft.motanx.rpc.cluster.LoadBalance;

import java.util.List;

/**
 * 快速失败
 * Created by kangwei on 2016/9/9.
 */
@Spi(name = "failfast")
public class FailfastHaStrategy extends AbstractHaStrategy implements HaStrategy {
    @Override
    protected <T> Response doCall(Request request, LoadBalance loadbalance, List<Provider<T>> providers) {
        Provider<T> referer = loadbalance.select(providers);
        return referer.invoke(request);
    }
}
