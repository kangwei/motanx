package com.opensoft.motanx.rpc.protocol.rest;

import com.google.common.collect.Maps;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.remote.http.HttpBinder;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.protocol.AbstractProtocol;
import com.opensoft.motanx.rpc.support.AbstractExporter;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

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

    @Override
    protected <T> T doRefer(Class<T> type, URL url) {
        URL copyUrl = url.copy();
        copyUrl.setProtocol("http");
        String address = copyUrl.getUri();
        return JAXRSClientFactory.create(address, type, Arrays.asList(new JacksonJsonProvider()));
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
