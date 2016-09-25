package com.opensoft.motanx.registry.support;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.registry.NotifyListener;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.rpc.support.AbstractNode;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 抽象的注册中心
 * Created by kangwei on 2016/9/2.
 */
public abstract class AbstractRegistry extends AbstractNode implements Registry {
    public static final Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);

    //防止多线程重复
    protected Set<URL> registried = Sets.newConcurrentHashSet();
    protected ConcurrentMap<URL, Set<NotifyListener>> subscribed = Maps.newConcurrentMap();
    protected ConcurrentMap<URL, List<URL>> notified = Maps.newConcurrentMap();

    public AbstractRegistry(URL url) {
        super(url);
    }

    @Override
    public void register(URL url) {
        if (url == null) {
            logger.warn("register url is null");
            return;
        }
        if (registried.contains(url)) {
            logger.warn("url : {} has been already registered", url.toString());
            return;
        }
        doRegister(url);
        registried.add(url);
        logger.info("register url : {} ", url.toString());
    }

    protected abstract void doRegister(URL url);

    @Override
    public void unRegister(URL url) {
        if (url == null) {
            logger.warn("unRegister url is null");
            return;
        }
        if (!registried.contains(url)) {
            logger.warn("url : {} has been already unregistered", url.toString());
            return;
        }

        doUnregister(url);
        registried.remove(url);
    }

    protected abstract void doUnregister(URL url);

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            logger.warn("subscribe url is null or notifyListener is null");
            return;
        }

        doSubscribe(url, listener);
        if (subscribed.containsKey(url)) {
            Set<NotifyListener> notifyListeners = subscribed.get(url);
            if (notifyListeners == null) {
                notifyListeners = Sets.newConcurrentHashSet();
            }
            notifyListeners.add(listener);
        } else {
            subscribed.putIfAbsent(url, Sets.<NotifyListener>newConcurrentHashSet());
            Set<NotifyListener> notifyListeners = subscribed.get(url);
            notifyListeners.add(listener);
        }
    }

    protected abstract void doSubscribe(URL url, NotifyListener listener);

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            logger.warn("unsubscribe url is null or notifyListener is null");
            return;
        }
        doUnsubscribe(url, listener);
        if (subscribed.containsKey(url)) {
            Set<NotifyListener> notifyListeners = subscribed.get(url);
            notifyListeners.remove(listener);
        }
    }

    protected abstract void doUnsubscribe(URL url, NotifyListener listener);

    protected void notify(URL url, NotifyListener listener, List<URL> urls) {
        logger.info("notify url : {}", url);
        notified.put(url, urls);
        listener.notify(url, urls);
    }

    @Override
    public List<URL> lookup(URL url) {
        if (url == null) {
            return Collections.emptyList();
        }
        if (notified == null || notified.isEmpty()) {
            return doLookup(url);
        }
        List<URL> urls = notified.get(url);
        return urls;
    }

    protected abstract List<URL> doLookup(URL url);

}
