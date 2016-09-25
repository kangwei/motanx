package com.opensoft.motanx.rpc.cluster.loadbalance;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载
 * Created by kangwei on 2016/9/9.
 */
@Spi(name = "random")
public class RandomLoadBalance extends AbstractLoadBalance implements LoadBalance {
    private Random random = new Random();

    @Override
    protected <T> Provider<T> doSelect(List<Provider<T>> providers) {
        int index = random.nextInt(providers.size());

        for (int i = 0; i < providers.size(); i++) {
            Provider<T> provider = providers.get((i + index) % providers.size());
            if (provider.isAvailable()) {
                return provider;
            }
        }
        throw new MotanxFrameworkException("no available provider");
    }
}
