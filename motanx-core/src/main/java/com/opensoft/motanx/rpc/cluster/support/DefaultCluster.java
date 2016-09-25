package com.opensoft.motanx.rpc.cluster.support;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.registry.NotifyListener;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.cluster.Cluster;
import com.opensoft.motanx.rpc.cluster.HaStrategy;
import com.opensoft.motanx.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 默认的集群实现
 * Created by kangwei on 2016/9/9.
 */
@Spi(name = "default")
public class DefaultCluster<T> implements Cluster<T>, NotifyListener {
    public static final Logger logger = LoggerFactory.getLogger(DefaultCluster.class);

    //通知时锁定，防止并发更新
    private final Lock lock = new ReentrantLock();
    private List<Provider<T>> referers;
    private LoadBalance loadbalance;
    private HaStrategy haStrategy;

    @Override
    public List<Provider<T>> getReferers() {
        return referers;
    }

    @Override
    public void setHaStrategy(HaStrategy haStrategy) {
        this.haStrategy = haStrategy;
    }

    @Override
    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadbalance = loadBalance;
    }

    @Override
    public void onRefresh(List<Provider<T>> referers) {
        this.referers = referers;
    }

    @Override
    public Response invoke(Request request) {
        return haStrategy.call(request, loadbalance, referers);
    }

    @Override
    public void notify(URL registryUrl, List<URL> urls) {
        try {
            lock.lock();
            if (CollectionUtils.isEmpty(urls)) {
                return;
            }
            List<Provider<T>> referers = Lists.newArrayList();
            for (URL url : urls) {
                Provider<T> refer = null;
                try {
                    Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(url.getProtocol());
                    Class<T> interfaceClass = (Class<T>) Class.forName(url.getPath());
                    refer = protocol.refer(interfaceClass, url);
                } catch (ClassNotFoundException e) {
                    logger.error("get refer error {}", e.getMessage(), e);
                }
                referers.add(refer);
            }

            onRefresh(referers);
        } finally {
            lock.unlock();
        }
    }
}
