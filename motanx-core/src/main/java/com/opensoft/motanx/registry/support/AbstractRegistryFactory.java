package com.opensoft.motanx.registry.support;

import com.google.common.collect.Maps;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.registry.RegistryFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 抽象的registry工厂
 * Created by kangwei on 2016/9/3.
 */
public abstract class AbstractRegistryFactory implements RegistryFactory {
    public static final Logger logger = LoggerFactory.getLogger(AbstractRegistryFactory.class);

    private static final ConcurrentMap<String, Registry> registryMap = Maps.newConcurrentMap();

    private static final Lock lock = new ReentrantLock();

    @Override
    public Registry getRegistry(URL url) {
        if (!registryMap.containsKey(url.getUri())) {
            try {
                lock.lock();
                if (!registryMap.containsKey(url.getUri())) {
                    registryMap.putIfAbsent(url.getUri(), createRegistry(url));
                }
            } finally {
                lock.unlock();
            }
        }
        return registryMap.get(url.getUri());
    }

    protected abstract Registry createRegistry(URL url);
}
