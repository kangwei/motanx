package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Provider;

/**
 * Created by kangwei on 2016/8/25.
 */
public abstract class AbstractExporter<T> implements Exporter<T> {
    protected Provider<T> provider;

    public AbstractExporter(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public Provider<T> getProvider() {
        return provider;
    }
}
