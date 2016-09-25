package com.opensoft.motanx.rpc.protocol.hessian;

import com.google.common.base.Strings;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.demo.AnotherService;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.AnotherServiceImpl;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.rpc.*;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kangwei on 2016/9/11.
 */
public class HessianProtocolTest {
    private URL url;
    private Provider<DemoService> provider;
    private DefaultRequest request;
    private Protocol protocol = new HessianProtocol();

    @Before
    public void prepare() {
        url = new URL("hessian", "localhost", 9000, "com.opensoft.motanx.demo.DemoService");
        url.setParameter(UrlConstants.server.getName(), "tomcat");
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.DemoService");
        request.setMethodName("hello");
    }

    @Test
    @Ignore
    public void test_on_stress() throws IOException, InterruptedException {
        protocol.export(provider);
//        final Protocol p = protocol;
        String s = Strings.repeat("a", 1024*50);
        request.setMethodName("hello");
        request.setParameterTypes(new Class[]{String.class});
        request.setArgs(new Object[]{s});
//        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
//        Response response = refer.invoke(request);
//        System.out.println(response.toString());
        for (int i = 0; i < 20; i++) {
            new Thread() {
                @Override
                public void run() {
                    Provider<DemoService> refer = protocol.refer(DemoService.class, url);
                    Response response = refer.invoke(request);
                    System.out.println(response.toString());
                }
            }.start();
        }
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void test_hessian_protocol() {
        protocol.export(provider);
        URL url1 = new URL("hessian", "localhost", 9000, "com.opensoft.motanx.demo.AnotherService");
        Provider<AnotherService> provider1 = new DefaultProvider<>(AnotherService.class, url1, new AnotherServiceImpl());
        protocol.export(provider1);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        Provider<AnotherService> anotherServiceProvider = protocol.refer(AnotherService.class, url1);

        Response response = refer.invoke(request);
        System.out.println(response.toString());
        if (response.hasException()) {
            response.getException().printStackTrace();
        }
        DefaultRequest request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.AnotherService");
        request.setMethodName("hello");
        Response invoke = anotherServiceProvider.invoke(request);
        System.out.println(invoke.toString());
        protocol.destroy();
    }

    @Test
    public void test_hessian_protocol_on_wrong_request() {
        url.setPort(9001);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Exporter<DemoService> exporter = protocol.export(provider);
        Assert.assertNotNull(exporter);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        request.setMethodName("sayHello");
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        protocol.destroy();
    }

    @Test
    public void test_hessian_protocol_on_pojo() {
        url.setPort(9002);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Exporter<DemoService> exporter = protocol.export(provider);
        Assert.assertNotNull(exporter);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        request.setMethodName("pojo");
        DemoPojo pojo = new DemoPojo();
        pojo.setName("haha");
        pojo.setAge(19);
        request.setParameterTypes(new Class[]{DemoPojo.class});
        request.setArgs(new Object[]{pojo});
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        protocol.destroy();
    }
}
