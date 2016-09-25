package com.opensoft.motanx.rpc.protocol.rest;

import com.google.common.base.Strings;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.demo.AnotherService;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.AnotherServiceImpl;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.protocol.hessian.HessianProtocol;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kangwei on 2016/9/12.
 */
public class RestProtocolTest {
    private URL url;
    private Provider<DemoService> provider;
    private DefaultRequest request;
    private Protocol protocol = new RestProtocol();

    @Before
    public void prepare() {
        url = new URL("rest", "localhost", 9000, "com.opensoft.motanx.demo.DemoService");
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
        String s = Strings.repeat("a", 1024);
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
    public void test_rest_protocol() throws IOException {
        Exporter<DemoService> exporter = protocol.export(provider);
        URL url1 = new URL("rest", "localhost", 9000, "com.opensoft.motanx.demo.AnotherService");
        Provider<AnotherService> provider1 = new DefaultProvider<>(AnotherService.class, url1, new AnotherServiceImpl());
        protocol.export(provider1);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        Provider<AnotherService> refer1 = protocol.refer(AnotherService.class, url1);
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        if (response.hasException()) {
            response.getException().printStackTrace();
        }
        DefaultRequest request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.AnotherService");
        request.setMethodName("hello");
        Response response1 = refer1.invoke(request);
        System.out.println(response1.toString());
        protocol.destroy();
    }

    @Test
    public void test_rest_protocol_on_path() {
        url.setPort(9001);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Exporter<DemoService> exporter = protocol.export(provider);
        Assert.assertNotNull(exporter);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        request.setMethodName("hello");
        request.setParameterTypes(new Class[]{String.class});
        request.setArgs(new Object[]{"12121"});
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        protocol.destroy();
    }

    @Test
    public void test_rest_protocol_on_pojo() {
        url.setPort(9010);
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
