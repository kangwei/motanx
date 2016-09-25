package com.opensoft.motanx.filter;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Invoker;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;

/**
 * Created by kangwei on 2016/9/24.
 */
@Spi(name = "accesslog")
public class AccessLogFilter implements Filter {
    public static final Logger log = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Response filter(Invoker invoker, Request request) {

        Response response = invoker.invoke(request);
        log.info("AccessLogFilter---->get response {} from request {}", response, request);
        return response;
    }
}
