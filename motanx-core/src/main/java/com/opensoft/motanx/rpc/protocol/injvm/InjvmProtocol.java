package com.opensoft.motanx.rpc.protocol.injvm;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.protocol.AbstractProtocol;
import com.opensoft.motanx.rpc.support.AbstractExporter;

/**
 * Created by kangwei on 2016/9/24.
 */
public class InjvmProtocol extends AbstractProtocol implements Protocol {
    private Exporter exporter;

    @Override
    protected <T> Exporter<T> doExport(Provider<T> provider) {
        exporter = new AbstractExporter(provider) {
            @Override
            public void unexport() {
                //do nothing
            }
        };
        return exporter;
    }

    @Override
    protected <T> Provider<T> doRefer(Class<T> type, URL url) {
        return exporter.getProvider();
    }

    @Override
    public int getDefaultPort() {
        return 0;
    }
}
