package com.opensoft.motanx.rpc.protocol.webservice;

import com.google.common.collect.Maps;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.proxy.jdk.JdkProxyFactory;
import com.opensoft.motanx.remote.http.HttpBinder;
import com.opensoft.motanx.remote.http.HttpHandler;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.remote.http.jetty.JettyHttpBinder;
import com.opensoft.motanx.remote.http.support.DispatcherServlet;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.protocol.AbstractProtocol;
import com.opensoft.motanx.rpc.protocol.hessian.HessianProtocol;
import com.opensoft.motanx.rpc.support.AbstractExporter;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.transport.http.DestinationRegistryImpl;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.ServletController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kangwei on 2016/9/11.
 */
@Spi(name = "webservice")
public class WebServiceProtocol extends AbstractProtocol implements Protocol {
    public static final Logger logger = LoggerFactory.getLogger(WebServiceProtocol.class);

    public static final int DEFAULT_PORT = 8181;

    private ConcurrentMap<String, HttpServer> servers = Maps.newConcurrentMap();

    private final HTTPTransportFactory transportFactory = new HTTPTransportFactory(new DestinationRegistryImpl());

    private HttpBinder binder;

    private class WebServiceHttpHandler implements HttpHandler {

        private volatile ServletController servletController;

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            if (servletController == null) {
                HttpServlet servlet = DispatcherServlet.getInstance();
                if (servlet == null) {
                    response.sendError(500, "No such DispatcherServlet instance.");
                    return;
                }
                synchronized (this) {
                    if (servletController == null) {
                        servletController = new ServletController(transportFactory.getRegistry(), servlet.getServletConfig(), servlet);
                    }
                }
            }

            servletController.invoke(request, response);
        }
    }

    @Override
    protected <T> Exporter<T> doExport(Provider<T> provider) {
        String address = provider.getUrl().getAddress();
        String server = provider.getUrl().getParameter(UrlConstants.server.getName(), UrlConstants.server.getString());
        HttpServer httpServer = servers.get(address);
        if (httpServer == null) {
            if (binder == null) {
                binder = ExtensionLoader.getExtensionLoader(HttpBinder.class).getExtension(server);
            }
            httpServer = binder.bind(provider.getUrl());
            DispatcherServlet.addHandler(provider.getUrl().getPort(), new WebServiceHttpHandler());
            httpServer.addServlet(DispatcherServlet.getInstance(), "/*");
            servers.putIfAbsent(address, httpServer);
        }
        final ServerFactoryBean factoryBean = new ServerFactoryBean();
        factoryBean.setAddress("/" + provider.getUrl().getPath());
        factoryBean.setServiceBean(provider.getImpl());
        factoryBean.setServiceClass(provider.getInterface());
        factoryBean.setDestinationFactory(transportFactory);
        factoryBean.create();
        return new AbstractExporter<T>(provider) {
            @Override
            public void unexport() {
                //do nothing
            }
        };
    }

    @Override
    protected <T> Provider<T> doRefer(Class<T> type, URL url) {
        ClientProxyFactoryBean proxyFactoryBean = new ClientProxyFactoryBean();
        URL copyUrl = url.copy();
        copyUrl.setProtocol("http");
        proxyFactoryBean.setAddress(copyUrl.getIdentifyUrl());
        proxyFactoryBean.setServiceClass(type);
        T t = (T) proxyFactoryBean.create();
        ProxyFactory proxy = new JdkProxyFactory();
        return proxy.getReferer(t, type, url);
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }
}
