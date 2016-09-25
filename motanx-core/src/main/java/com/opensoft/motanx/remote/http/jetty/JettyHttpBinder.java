package com.opensoft.motanx.remote.http.jetty;

import com.opensoft.motanx.core.Scope;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.remote.http.HttpBinder;
import com.opensoft.motanx.remote.http.HttpHandler;
import com.opensoft.motanx.remote.http.HttpServer;

/**
 * Created by kangwei on 2016/9/11.
 */
@Spi(name = "jetty", scope = Scope.SINGLETON)
public class JettyHttpBinder implements HttpBinder {
    @Override
    public HttpServer bind(URL url) {
        return new JettyHttpServer(url);
    }
}
