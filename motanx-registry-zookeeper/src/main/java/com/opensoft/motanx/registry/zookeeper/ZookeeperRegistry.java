package com.opensoft.motanx.registry.zookeeper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensoft.motanx.core.MotanxConstants;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.registry.NotifyListener;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.registry.support.FailRetryRegistry;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static com.opensoft.motanx.utils.RegistryPathUtils.toNodeParentPath;
import static com.opensoft.motanx.utils.RegistryPathUtils.toUrlPath;

/**
 * Zookeeper注册中心
 * Created by kangwei on 2016/9/3.
 */
public class ZookeeperRegistry extends FailRetryRegistry implements Registry {
    private ZkClient zkClient;

    private ConcurrentMap<URL, ConcurrentMap<NotifyListener, IZkChildListener>> zkListeners = Maps.newConcurrentMap();

    public ZookeeperRegistry(URL url, ZkClient zkClient) {
        super(url);
        this.zkClient = zkClient;
        IZkStateListener zkStateListener = new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                //do nothing
            }

            @Override
            public void handleNewSession() throws Exception {
                recover();
            }
        };
        zkClient.subscribeStateChanges(zkStateListener);
    }

    @Override
    protected void doRegister(URL url) {
        removeNode(url);
        createNode(url);
    }

    private void createNode(URL url) {
        String parentPath = toNodeParentPath(url);
        if (!zkClient.exists(parentPath)) {
            zkClient.createPersistent(parentPath, true);
        }
        zkClient.createEphemeral(toUrlPath(url), url.getIdentifyUrl());
    }

    private void removeNode(URL url) {
        String urlPath = toUrlPath(url);
        if (zkClient.exists(urlPath)) {
            zkClient.delete(urlPath);
        }
    }

    @Override
    protected void doUnregister(URL url) {
        removeNode(url);
    }

    @Override
    protected void doSubscribe(final URL url, final NotifyListener listener) {
        ConcurrentMap<NotifyListener, IZkChildListener> childrenListenerMap = zkListeners.get(url);
        if (childrenListenerMap == null) {
            zkListeners.putIfAbsent(url, Maps.<NotifyListener, IZkChildListener>newConcurrentMap());
            childrenListenerMap = zkListeners.get(url);
        }
        IZkChildListener iZkChildListener = childrenListenerMap.get(listener);
        if (iZkChildListener == null) {
            childrenListenerMap.putIfAbsent(listener, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentNode, List<String> children) throws Exception {
                    ZookeeperRegistry.this.notify(url, listener, path2Url(parentNode, children));
                }
            });
        }
        notify(url, listener, lookup(url));
    }

    private List<URL> path2Url(String nodeParentPath, List<String> children) {
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        List<URL> urls = Lists.newArrayList();
        for (String child : children) {
            String data = zkClient.readData(nodeParentPath + MotanxConstants.PATH_SEPARATOR + child);
            urls.add(URL.valueOf(data));
        }

        return urls;
    }

    @Override
    protected void doUnsubscribe(URL url, NotifyListener listener) {
        ConcurrentMap<NotifyListener, IZkChildListener> zkChildListenerConcurrentMap = zkListeners.get(url);
        if (zkChildListenerConcurrentMap != null) {
            zkChildListenerConcurrentMap.remove(listener);
        }
    }

    @Override
    protected List<URL> doLookup(URL url) {
        String nodeParentPath = toNodeParentPath(url);

        List<String> children = zkClient.getChildren(nodeParentPath);
        return path2Url(nodeParentPath, children);
    }

    @Override
    protected boolean doInit() {
        return true;
    }

    @Override
    protected boolean doDestroy() {
        zkClient.close();
        return true;
    }
}
