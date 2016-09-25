package com.opensoft.motanx.rpc;

/**
 * Invoker
 * Created by kangwei on 2016/6/26.
 */
public interface Invoker<T> {

    /**
     * RPC服务调用
     *
     * @param request 请求
     * @return 响应
     */
    Response invoke(Request request);
}
