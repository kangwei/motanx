package com.opensoft.motanx.remote.http.support;

import com.google.common.collect.Maps;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.remote.http.HttpHandler;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.rpc.support.AbstractNode;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kangwei on 2016/9/11.
 */
public abstract class AbstractHttpServer extends AbstractNode implements HttpServer {
    protected ConcurrentMap<String, Servlet> servletMap = Maps.newConcurrentMap();

    public AbstractHttpServer(URL url) {
        super(url);
    }

    @Override
    public void addServlet(Servlet servlet, String path) {
        if (servletMap.containsKey(path)) {
            throw new MotanxFrameworkException("multiple banding with path" + path);
        }

        servletMap.putIfAbsent(path, servlet);
        doAddServlet(servlet, path);
    }

    protected abstract void doAddServlet(Servlet servlet, String path);
}
