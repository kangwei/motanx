package com.opensoft.motanx.remote.http.tomcat;

import com.opensoft.motanx.core.Scope;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.remote.http.HttpBinder;
import com.opensoft.motanx.remote.http.HttpServer;

/**
 * Created by kangwei on 2016/9/23.
 */
@Spi(name = "tomcat", scope = Scope.SINGLETON)
public class TomcatHttpBinder implements HttpBinder {
    @Override
    public HttpServer bind(URL url) {
        return new TomcatHttpServer(url);
    }
}
