package com.opensoft.motanx.rpc.protocol;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.support.DefaultProvider;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 抽象的协议实现
 * Created by kangwei on 2016/8/25.
 */
public abstract class AbstractProtocol implements Protocol {
    public static final Logger logger = LoggerFactory.getLogger(AbstractProtocol.class);

    private ConcurrentMap<String, Exporter> exporterMap = Maps.newConcurrentMap();

    private List<Provider> providers = Lists.newCopyOnWriteArrayList();

    @Override
    public <T> Exporter<T> export(Provider<T> provider) {
        if (provider == null) {
            throw new MotanxFrameworkException("provider is null");
        }

        if (provider.getUrl() == null) {
            throw new MotanxFrameworkException("provider " + provider.getClass().getName() + " url is null");
        }
        URL providerUrl = provider.getUrl();
        if (!exporterMap.containsKey(providerUrl.getUri())) {
            Exporter<T> exporter = doExport(provider);
            exporterMap.putIfAbsent(providerUrl.getUri(), exporter);
        }
        return exporterMap.get(providerUrl.getUri());
    }

    protected abstract <T> Exporter<T> doExport(Provider<T> provider);

    @Override
    public <T> Provider<T> refer(Class<T> type, URL url) {
        if (type == null) {
            throw new MotanxFrameworkException("interface class is null");
        }

        if (url == null) {
            throw new MotanxFrameworkException("refer url is null");
        }
        T t = doRefer(type, url);
        Provider<T> referer = new DefaultProvider<>(type, url, t);
        providers.add(referer);
        return referer;
    }

    protected abstract <T> T doRefer(Class<T> type, URL url);

    @Override
    public void destroy() {
        for (String key : exporterMap.keySet()) {
            Exporter exporter = exporterMap.get(key);
            if (exporter != null)
            exporter.unexport();
        }

        for (Provider provider : providers) {
            if (provider != null)
                provider.destroy();
        }

        logger.info("protocol " + this.getClass().getSimpleName() + " destroy success");
    }
}
