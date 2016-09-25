package com.opensoft.motanx.rpc.protocol.rest;

import com.opensoft.motanx.demo.AnotherService;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.AnotherServiceImpl;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kangwei on 2016/9/20.
 */
public class RestTest {
    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);

        Context root = new Context(contexts, "/", Context.SESSIONS);

        RestApplication application = new RestApplication();
        application.addResourceClasses(DemoService.class);
        application.addResourceClasses(AnotherService.class);
        application.addResourceProvider(new DemoServiceImpl());
        application.addResourceProvider(new AnotherServiceImpl());
        application.addResourceProvider(new JacksonJsonProvider());
        CXFNonSpringJaxrsServlet cxf = new CXFNonSpringJaxrsServlet(application);
        ServletHolder servlet = new ServletHolder(cxf);
//        servlet.setInitParameter("javax.ws.rs.Application", application.getClass().getName());
//        servlet.setName("services");
//        servlet.setForcedPath("services");
        root.addServlet(servlet, "/" + DemoService.class.getName() + "/*");
        server.start();
//        server.join();


        DemoService demoService = JAXRSClientFactory.create("http://localhost:9000/com.opensoft.motanx.demo.DemoService", DemoService.class, Arrays.asList(new JacksonJsonProvider()));
        List<String> list = demoService.list();
        System.out.println(list);
    }
}
