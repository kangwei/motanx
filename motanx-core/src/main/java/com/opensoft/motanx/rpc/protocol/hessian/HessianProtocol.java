package com.opensoft.motanx.rpc.protocol.hessian;

import com.alibaba.dubbo.rpc.protocol.hessian.HttpClientConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.server.HessianSkeleton;
import com.google.common.collect.Maps;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.proxy.jdk.JdkProxyFactory;
import com.opensoft.motanx.remote.http.HttpBinder;
import com.opensoft.motanx.remote.http.HttpHandler;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.remote.http.support.DispatcherServlet;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.protocol.AbstractProtocol;
import com.opensoft.motanx.rpc.support.AbstractExporter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kangwei on 2016/9/11.
 */
@Spi(name = "hessian")
public class HessianProtocol extends AbstractProtocol implements Protocol {
    public static final Logger logger = LoggerFactory.getLogger(HessianProtocol.class);

    public static final int DEFAULT_PORT = 8010;

    private ConcurrentMap<String, HttpServer> servers = Maps.newConcurrentMap();

    private ConcurrentMap<String, HessianSkeleton> skeletons = Maps.newConcurrentMap();

    private HttpBinder binder;

    private class HessianHandler implements HttpHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            String uri = request.getRequestURI();
            HessianSkeleton skeleton = skeletons.get(uri);
            try {
                skeleton.invoke(request.getInputStream(), response.getOutputStream());
            } catch (Exception e) {
                throw new ServletException(e);
            }
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
            DispatcherServlet.addHandler(provider.getUrl().getPort(), new HessianHandler());
            httpServer.addServlet(DispatcherServlet.getInstance(), "/*");
            servers.putIfAbsent(address, httpServer);
        }

        final String path = "/" + provider.getUrl().getPath();
        HessianSkeleton hessianSkeleton = skeletons.get(path);
        if (hessianSkeleton == null) {
            hessianSkeleton = new HessianSkeleton(provider.getImpl(), provider.getInterface());
            skeletons.putIfAbsent(path, hessianSkeleton);
        }
        return new AbstractExporter<T>(provider) {
            @Override
            public void unexport() {
                skeletons.remove(path);
            }
        };
    }

    @Override
    protected <T> T doRefer(Class<T> type, URL url) {
        HessianProxyFactory proxyFactory = new HessianProxyFactory();
        proxyFactory.setOverloadEnabled(true);
//        proxyFactory.setConnectionFactory(new HttpClientConnectionFactory());
        int timeout = url.getIntParameter(UrlConstants.connectionTimeout.getName(), UrlConstants.connectionTimeout.getInt());
        proxyFactory.setConnectTimeout(timeout);
        proxyFactory.setReadTimeout(timeout);

        URL copyUrl = url.copy();
        copyUrl.setProtocol("http");

        try {
            return  (T) proxyFactory.create(type, new java.net.URL(copyUrl.getIdentifyUrl()), this.getClass().getClassLoader());
        } catch (MalformedURLException e) {
            throw new MotanxFrameworkException(e.getMessage(), e);
        }
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (HttpServer server : servers.values()) {
            server.destroy();
        }
    }
}
