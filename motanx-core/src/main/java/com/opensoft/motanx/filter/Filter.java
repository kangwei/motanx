package com.opensoft.motanx.filter;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.rpc.Invoker;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;

/**
 * 过滤器，拦截请求
 * Created by kangwei on 2016/9/24.
 */
@Spi
public interface Filter {
    Response filter(Invoker invoker, Request request);
}
