package com.opensoft.motanx.rpc.protocol.motanx;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.protocol.AbstractProtocol;

/**
 * 默认的协议
 * Created by kangwei on 2016/9/25.
 */
@Spi(name = "motanx")
public class MotanxProtocol extends AbstractProtocol implements Protocol {
    @Override
    protected <T> Exporter<T> doExport(Provider<T> provider) {
        return null;
    }

    @Override
    protected <T> T doRefer(Class<T> type, URL url) {
        return null;
    }

    @Override
    public int getDefaultPort() {
        return 0;
    }
}
