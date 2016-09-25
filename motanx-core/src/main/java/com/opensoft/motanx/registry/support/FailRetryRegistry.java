package com.opensoft.motanx.registry.support;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opensoft.motanx.core.NamedThreadFactory;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.registry.NotifyListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 失败重试注册中心
 * Created by kangwei on 2016/9/3.
 */
public abstract class FailRetryRegistry extends AbstractRegistry {
    public static final Logger logger = LoggerFactory.getLogger(FailRetryRegistry.class);

    private final Set<URL> failedRegistered = Sets.newConcurrentHashSet();
    private final Set<URL> failedUnregistered = Sets.newConcurrentHashSet();
    private final ConcurrentMap<URL, Set<NotifyListener>> failedSubscribed = Maps.newConcurrentMap();
    private final ConcurrentMap<URL, Set<NotifyListener>> failedUnSubscribed = Maps.newConcurrentMap();
    private final ScheduledExecutorService retryExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("retryRegistry"));

    public FailRetryRegistry(URL url) {
        super(url);
        long delay = url.getLongParameter(UrlConstants.delay.getName(), UrlConstants.delay.getLong());
        retryExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                retry();
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void register(URL url) {
        try {
            super.register(url);
            failedRegistered.remove(url);
            //防止unregister
            failedUnregistered.remove(url);
        } catch (Exception e) {
            logger.error("register url : {} error, will be retry", url, e);
            failedRegistered.add(url);
        }
    }

    @Override
    public void unRegister(URL url) {
        try {
            super.unRegister(url);
            failedUnregistered.remove(url);
            //防止重新register
            failedRegistered.remove(url);
        } catch (Exception e) {
            logger.error("unRegister url : {} error, will be retry", url, e);
            failedUnregistered.add(url);
        }
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        try {
            super.subscribe(url, listener);
            removeFailed(url, listener);
        } catch (Exception e) {
            logger.error("subscribe url : {} error, will be retry", url, e);
            addFail(failedSubscribed, url, listener);
        }
    }

    private void addFail(ConcurrentMap<URL, Set<NotifyListener>> map, URL url, NotifyListener listener) {
        if (map.containsKey(url)) {
            Set<NotifyListener> notifyListeners = map.get(url);
            if (notifyListeners == null) {
                map.putIfAbsent(url, Sets.<NotifyListener>newConcurrentHashSet());
                notifyListeners = map.get(url);
            }
            notifyListeners.add(listener);
        } else {
            map.putIfAbsent(url, Sets.<NotifyListener>newConcurrentHashSet());
            Set<NotifyListener> notifyListeners = map.get(url);
            notifyListeners.add(listener);
        }
    }

    private void removeFailed(URL url, NotifyListener listener) {
        Set<NotifyListener> notifyListeners = failedSubscribed.get(url);
        if (notifyListeners != null) {
            notifyListeners.remove(listener);
        }

        Set<NotifyListener> listeners = failedUnSubscribed.get(url);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        try {
            super.unsubscribe(url, listener);
            removeFailed(url, listener);
        } catch (Exception e) {
            logger.error("unsubscribe url : {} error, will be retry", url, e);
            addFail(failedUnSubscribed, url, listener);
        }
    }

    protected void recover() {
        if (!registried.isEmpty()) {
            for (URL url : failedRegistered) {
                register(url);
            }
        }

        if (!subscribed.isEmpty()) {
            for (URL url : subscribed.keySet()) {
                Set<NotifyListener> notifyListeners = subscribed.get(url);
                if (notifyListeners != null && !notifyListeners.isEmpty()) {
                    for (NotifyListener notifyListener : notifyListeners) {
                        subscribe(url, notifyListener);
                    }
                }
            }
        }
    }

    private void retry() {
        if (logger.isDebugEnabled()) {
            logger.debug("begin to retry failed");
        }

        if (!failedRegistered.isEmpty()) {
            Set<URL> urls = new HashSet<>(failedRegistered);
            for (URL url : urls) {
                try {
                    register(url);
                } catch (Exception e) { //忽略异常，等待下次重试
                    logger.warn("url : {} register error", url, e);
                }
            }
        }

        if (!failedUnregistered.isEmpty()) {
            Set<URL> urls = new HashSet<>(failedUnregistered);
            for (URL url : urls) {
                try {
                    unRegister(url);
                } catch (Exception e) {
                    logger.warn("url : {} unRegister error", url, e);
                }

            }
        }

        if (!failedSubscribed.isEmpty()) {
            HashMap<URL, Set<NotifyListener>> newHashMap = Maps.newHashMap(failedSubscribed);
            for (Map.Entry<URL, Set<NotifyListener>> entry : newHashMap.entrySet()) {
                Set<NotifyListener> listeners = entry.getValue();
                if (listeners == null || listeners.isEmpty()) {
                    newHashMap.remove(entry.getKey());
                }
            }

            if (!newHashMap.isEmpty()) {
                for (URL url : newHashMap.keySet()) {
                    Set<NotifyListener> notifyListeners = newHashMap.get(url);
                    for (NotifyListener notifyListener : notifyListeners) {
                        try {
                            subscribe(url, notifyListener);
                        } catch (Exception e) {
                            logger.warn("url : {} subscribe error", url, e);
                        }
                    }
                }
            }
        }

        if (!failedUnSubscribed.isEmpty()) {
            HashMap<URL, Set<NotifyListener>> newHashMap = Maps.newHashMap(failedUnSubscribed);
            for (Map.Entry<URL, Set<NotifyListener>> entry : newHashMap.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    newHashMap.remove(entry.getKey());
                }
            }

            if (!newHashMap.isEmpty()) {
                for (URL url : newHashMap.keySet()) {
                    Set<NotifyListener> notifyListeners = newHashMap.get(url);
                    for (NotifyListener notifyListener : notifyListeners) {
                        try {
                            unsubscribe(url, notifyListener);
                        } catch (Exception e) {
                            logger.warn("url : {} unsubscribe error", url, e);
                        }
                    }
                }
            }
        }
    }


}
