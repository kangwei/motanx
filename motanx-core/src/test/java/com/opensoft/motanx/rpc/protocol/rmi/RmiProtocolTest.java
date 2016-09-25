package com.opensoft.motanx.rpc.protocol.rmi;

import com.google.common.base.Strings;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kangwei on 2016/8/27.
 */
public class RmiProtocolTest {
    private URL url;
    private Provider<DemoService> provider;
    private DefaultRequest request;

    @Before
    public void prepare() {
        url = new URL("rmi", "localhost", 1099, "com.opensoft.motanx.demo.DemoService");
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.DemoService");
        request.setMethodName("hello");
    }

    @Test
    @Ignore
    public void test_on_stress() throws IOException, InterruptedException {
        final Protocol protocol = new RmiProtocol();
        protocol.export(provider);
//        final Protocol p = protocol;
        String s = Strings.repeat("a", 1024 * 1);
        System.out.println(s.getBytes().length);
        request.setMethodName("hello");
        request.setParameterTypes(new Class[]{String.class});
        request.setArgs(new Object[]{s});
//        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
//        Response response = refer.invoke(request);
//        System.out.println(response.toString());
        for (int i = 0; i < 100; i++) {
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
    public void test_rmi_protocol() {
        Protocol protocol = new RmiProtocol();
        Exporter<DemoService> exporter = protocol.export(provider);
        Assert.assertNotNull(exporter);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        protocol.destroy();
    }

    @Test
    public void test_rmi_protocol_on_wrong_request() {
        Protocol protocol = new RmiProtocol();
        Exporter<DemoService> exporter = protocol.export(provider);
        Assert.assertNotNull(exporter);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        request.setMethodName("sayHello");
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        protocol.destroy();
    }

    @Test
    public void test_rmi_protocol_on_pojo() {
        Protocol protocol = new RmiProtocol();
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
