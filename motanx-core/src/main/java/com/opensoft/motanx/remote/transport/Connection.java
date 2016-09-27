package com.opensoft.motanx.remote.transport;

import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;

/**
 * Created by kangwei on 2016/9/26.
 */
public interface Connection {
    void init();
    void connect(String host,int port);
    Response invoke(Request request, boolean async);
    void close();
    boolean isConnected();
    boolean isClosed();
}
