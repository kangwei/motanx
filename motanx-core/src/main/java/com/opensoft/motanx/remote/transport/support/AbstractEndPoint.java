package com.opensoft.motanx.remote.transport.support;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.remote.transport.Endpoint;
import com.opensoft.motanx.rpc.support.AbstractNode;

import java.net.InetSocketAddress;

/**
 * 抽象的endpoint实现
 * Created by kangwei on 2016/9/26.
 */
public abstract class AbstractEndPoint extends AbstractNode implements Endpoint {
    protected InetSocketAddress localAddress;
    protected InetSocketAddress remoteAddress;
    protected boolean isClosed = true;

    public AbstractEndPoint(URL url) {
        super(url);
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void close() {
        isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}
