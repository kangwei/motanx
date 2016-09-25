package com.opensoft.motanx.rpc.protocol.rest;

import com.google.common.collect.Maps;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
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
import com.opensoft.motanx.rpc.support.AbstractExporter;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.transport.http.DestinationRegistryImpl;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.http.HttpDestinationFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.ServletDestinationFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kangwei on 2016/9/12.
 */
@Spi(name = "rest")
public class RestProtocol extends AbstractProtocol implements Protocol {
    public static final Logger logger = LoggerFactory.getLogger(RestProtocol.class);

    public static final int DEFAULT_PORT = 80;

    private ConcurrentMap<String, HttpServer> servers = Maps.newConcurrentMap();

    private HttpBinder binder;

    private RestApplication application = new RestApplication();

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
            application.addResourceClasses(provider.getInterface());
            application.addResourceProvider(provider.getImpl());
            application.addResourceProvider(new JacksonJsonProvider());
//            application.addResourceProvider(new GsonJsonProvider());
            CXFNonSpringJaxrsServlet cxf = new CXFNonSpringJaxrsServlet(application);
            httpServer.addServlet(cxf, "/" + provider.getUrl().getPath() + "/*");
            servers.putIfAbsent(address, httpServer);
        } else {
            application.addResourceClasses(provider.getInterface());
            application.addResourceProvider(provider.getImpl());
//            application.addResourceProvider(new JacksonJsonProvider());
//            application.addResourceProvider(new GsonJsonProvider());
            CXFNonSpringJaxrsServlet cxf = new CXFNonSpringJaxrsServlet(application);
            httpServer.addServlet(cxf, "/" + provider.getUrl().getPath() + "/*");
        }

//        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
//        sf.setResourceClasses(provider.getInterface());
//        sf.setResourceProvider(provider.getInterface(), new SingletonResourceProvider(provider.getImpl()));
//        sf.setAddress("http://localhost:9000/");
//        sf.setProvider(new JacksonJsonProvider());
//        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
//        sf.setBus(bus);
//        sf.setDestinationFactory(transportFactory);
//        JAXRSBindingFactory factory = new JAXRSBindingFactory();
//        factory.setBus(sf.getBus());
//        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);
//        sf.create();
        return new AbstractExporter<T>(provider) {
            @Override
            public void unexport() {
                //do nothing
            }
        };
    }

    public static void main(String[] args) throws Exception {
        Server httpServer = new Server(9000);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        httpServer.setHandler(contexts);

        Context root = new Context(contexts, "/", Context.SESSIONS);

        CXFNonSpringServlet cxf = new CXFNonSpringServlet();
        ServletHolder servlet = new ServletHolder(cxf);
        servlet.setName("rest");
        servlet.setForcedPath("rest");
        root.addServlet(servlet, "/*");
        httpServer.start();

        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(DemoService.class);
        sf.setResourceProvider(DemoService.class, new SingletonResourceProvider(new DemoServiceImpl()));
        sf.setAddress("http://localhost:9000/");
        sf.setProvider(new JacksonJsonProvider());
//        Bus bus = cxf.getBus();
//        sf.setBus(bus);
//        BusFactory.setDefaultBus(bus);
//        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
//        ExtensionManagerBus bus = new ExtensionManagerBus();
//        sf.setBus(bus);
//        sf.setDestinationFactory(new HTTPTransportFactory(new DestinationRegistryImpl()));
//        JAXRSBindingFactory factory = new JAXRSBindingFactory();
//        factory.setBus(sf.getBus());
//        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);
        sf.create();
//        Bus bus = cxf.getBus();
//        BusFactory.setDefaultBus(bus);
//        DemoService impl = new DemoServiceImpl();
//        Endpoint.publish("/rest", impl);
    }

    @Override
    protected <T> Provider<T> doRefer(Class<T> type, URL url) {
        URL copyUrl = url.copy();
        copyUrl.setProtocol("http");
        String address = copyUrl.getUri();
        T t = JAXRSClientFactory.create(address, type, Arrays.asList(new JacksonJsonProvider()));
        ProxyFactory proxy = new JdkProxyFactory();
        return proxy.getReferer(t, type, url);
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }
}
