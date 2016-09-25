package com.opensoft.motanx.rpc;

import java.net.InetSocketAddress;

/**
 * 记录服务调用的状态和上下文环境，每次调用，服务状态均会改变
 * ThreadLocal，ThreadSafe
 * Created by kangwei on 2016/8/25.
 */
public class RpcContext {
    private static ThreadLocal<RpcContext> CONTEXT = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    private Request request;
    private InetSocketAddress remoteAddress;
    private InetSocketAddress localAddress;
    private long startTime;

    public static RpcContext getContext() {
        return CONTEXT.get();
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public long getStartTime() {
        return startTime == 0 ? System.currentTimeMillis() : startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
