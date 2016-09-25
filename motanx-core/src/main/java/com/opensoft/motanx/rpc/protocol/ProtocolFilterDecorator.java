package com.opensoft.motanx.rpc.protocol;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.filter.Filter;
import com.opensoft.motanx.rpc.*;
import com.opensoft.motanx.rpc.support.AbstractProvider;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * protocol filter装饰器
 * Created by kangwei on 2016/9/24.
 */
public class ProtocolFilterDecorator implements Protocol {
    private Protocol protocol;

    public ProtocolFilterDecorator(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public <T> Exporter<T> export(Provider<T> provider) {
        return protocol.export(decorateWithFilter(provider, provider.getUrl()));
    }

    private <T> Provider<T> decorateWithFilter(Provider<T> provider, URL url) {
        String filters = url.getParameter(UrlConstants.filters.getName());
        if (StringUtils.isBlank(filters)) {
            return provider;
        }

        List<Filter> filterList = getFilters(filters);
        Provider last = provider;
        for (Filter filter : filterList) {
            final Filter f = filter;
            final Provider p = last;
            last = new AbstractProvider(url, provider.getInterface(), provider.getImpl()) {
                @Override
                public Response doInvoke(Method method, Request request) throws Exception {
                    return f.filter(p, request);
                }
            };
        }
        return last;
    }

    private List<Filter> getFilters(String filters) {
        List<Filter> filterList = Lists.newArrayList();
        List<String> list = Splitter.on(",").splitToList(filters);
        for (String s : list) {
            Filter filter = ExtensionLoader.getExtensionLoader(Filter.class).getExtension(s);
            addIfAbsent(filterList, filter);
        }
        return filterList;
    }

    private void addIfAbsent(List<Filter> filterList, Filter filter) {
        boolean isExists = false;
        for (Filter f : filterList) {
            if (f.getClass().equals(filter.getClass())) {
                isExists = true;
                break;
            }
        }
        if (!isExists) {
            filterList.add(filter);
        }
    }

    @Override
    public <T> Provider<T> refer(Class<T> type, URL url) {
        return decorateWithFilter(protocol.refer(type, url), url);
    }

    @Override
    public int getDefaultPort() {
        return protocol.getDefaultPort();
    }

    @Override
    public void destroy() {
        protocol.destroy();
    }
}
