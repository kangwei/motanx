package com.opensoft.motanx.rpc.cluster.hastrategy;

import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.cluster.HaStrategy;
import com.opensoft.motanx.rpc.cluster.LoadBalance;
import com.opensoft.motanx.rpc.support.DefaultResponse;

import java.util.List;

/**
 * 抽象的高可用调用策略实现
 * Created by kangwei on 2016/9/9.
 */
public abstract class AbstractHaStrategy implements HaStrategy {
    @Override
    public <T> Response call(Request request, LoadBalance loadbalance, List<Provider<T>> providers) {
        try {
            return doCall(request, loadbalance, providers);
        } catch (Exception e) {
            DefaultResponse response = new DefaultResponse();
            response.setException(e);
            return response;
        }
    }

    protected abstract <T> Response doCall(Request request, LoadBalance loadbalance, List<Provider<T>> providers);
}
