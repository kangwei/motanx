package com.opensoft.motanx.rpc;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;

/**
 * RPC协议，SPI
 * Created by kangwei on 2016/8/24.
 */
@Spi
public interface Protocol {
    <T> Exporter<T> export(Provider<T> provider);

    <T> Provider<T> refer(Class<T> type, URL url);

    int getDefaultPort();

    void destroy();
}
