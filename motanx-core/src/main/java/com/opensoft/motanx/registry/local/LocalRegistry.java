package com.opensoft.motanx.registry.local;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.registry.NotifyListener;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.registry.support.AbstractRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.opensoft.motanx.utils.RegistryPathUtils.toNodeParentPath;

/**
 * 本地注册中心
 * Created by kangwei on 2016/9/4.
 */
@Spi
public class LocalRegistry extends AbstractRegistry implements Registry {
    private static final ConcurrentMap<String, Set<URL>> registyStore = Maps.newConcurrentMap();
    private static final ConcurrentMap<String, Map<URL, Set<NotifyListener>>> subscribeStore = Maps.newConcurrentMap();

    private static final Lock serverLock = new ReentrantLock();
    private static final Lock clientLock = new ReentrantLock();

    public LocalRegistry(URL url) {
        super(url);
    }

    @Override
    protected void doRegister(URL url) {
        String nodePath = toNodeParentPath(url);
        try {
            serverLock.lock();
            Set<URL> urls = registyStore.get(nodePath);
            if (urls == null) {
                registyStore.putIfAbsent(nodePath, Sets.<URL>newConcurrentHashSet());
                urls = registyStore.get(nodePath);
            }
            urls.add(url);
            notifyAllClient(nodePath, urls);
        } finally {
            serverLock.unlock();
        }
    }

    private void notifyAllClient(String nodePath, Set<URL> urls) {
        Map<URL, Set<NotifyListener>> urlSetMap = subscribeStore.get(nodePath);
        if (urlSetMap != null && !urlSetMap.isEmpty()) {
            for (Map.Entry<URL, Set<NotifyListener>> entry : urlSetMap.entrySet()) {
                URL refererUrl = entry.getKey();
                Set<NotifyListener> listeners = entry.getValue();
                if (listeners != null && !listeners.isEmpty()) {
                    for (NotifyListener listener : listeners) {
                        notify(refererUrl, listener, Lists.newArrayList(urls));
                    }
                }
            }
        }
    }

    @Override
    protected void doUnregister(URL url) {
        try {
            serverLock.lock();
            Set<URL> urls = registyStore.get(toNodeParentPath(url));
            if (urls != null) {
                urls.remove(url);
            }
            notifyAllClient(toNodeParentPath(url), urls);
        } finally {
            serverLock.unlock();
        }
    }

    @Override
    protected void doSubscribe(URL url, NotifyListener listener) {
        try {
            clientLock.lock();
            String key = toNodeParentPath(url);
            Map<URL, Set<NotifyListener>> urlSetMap = subscribeStore.get(key);
            if (urlSetMap == null) {
                subscribeStore.putIfAbsent(key, Maps.<URL, Set<NotifyListener>>newConcurrentMap());
                urlSetMap = subscribeStore.get(key);
            }

            Set<NotifyListener> notifyListeners = urlSetMap.get(url);
            if (notifyListeners == null) {
                urlSetMap.put(url, Sets.<NotifyListener>newConcurrentHashSet());
                notifyListeners = urlSetMap.get(url);
            }

            notifyListeners.add(listener);
            notify(url, listener, lookup(url));
        } finally {
            clientLock.unlock();
        }
    }

    @Override
    protected void doUnsubscribe(URL url, NotifyListener listener) {
        try {
            clientLock.lock();
            Map<URL, Set<NotifyListener>> urlSetMap = subscribeStore.get(toNodeParentPath(url));
            if (urlSetMap != null) {
                Set<NotifyListener> notifyListeners = urlSetMap.get(url);
                if (notifyListeners != null) {
                    notifyListeners.remove(listener);
                }
            }
        } finally {
            clientLock.unlock();
        }
    }

    @Override
    protected List<URL> doLookup(URL url) {
        System.out.println(registyStore);
        Set<URL> urls = registyStore.get(toNodeParentPath(url));
        if (urls == null) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(urls);
    }

    @Override
    protected boolean doInit() {
        return true;
    }

    @Override
    protected boolean doDestroy() {
        return true;
    }
}
