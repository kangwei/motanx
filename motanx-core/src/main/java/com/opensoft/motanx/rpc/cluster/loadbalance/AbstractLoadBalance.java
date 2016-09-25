package com.opensoft.motanx.rpc.cluster.loadbalance;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.cluster.LoadBalance;

import java.util.List;

/**
 * 抽象的负载均衡实现
 * Created by kangwei on 2016/9/9.
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public <T> Provider<T> select(List<Provider<T>> providers) {
        if (CollectionUtils.isEmpty(providers)) {
            throw new MotanxFrameworkException("no provider exists");
        }

        if (providers.size() == 1) {
            Provider<T> provider = providers.get(0);
            if (provider.isAvailable()) {
                return provider;
            } else {
                throw new MotanxFrameworkException("no available provider");
            }
        }

        return doSelect(providers);
    }

    /**
     * 具体的负载均衡算法
     *
     * @param providers
     * @param <T>
     * @return
     */
    protected abstract <T> Provider<T> doSelect(List<Provider<T>> providers);
}
