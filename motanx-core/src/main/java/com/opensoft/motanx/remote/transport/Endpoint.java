package com.opensoft.motanx.remote.transport;

import com.opensoft.motanx.exception.MotanxRpcException;
import com.opensoft.motanx.rpc.Node;

import java.net.InetSocketAddress;

/**
 * Created by kangwei on 2016/9/26.
 */
public interface Endpoint extends Node {
    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    void send(Object message) throws MotanxRpcException;

    void close();

    boolean isClosed();
}
