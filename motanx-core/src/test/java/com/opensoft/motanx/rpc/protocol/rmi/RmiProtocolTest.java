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
import com.opensoft.motanx.rpc.protocol.ProtocolBaseTest;
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
public class RmiProtocolTest extends ProtocolBaseTest {

    @Before
    public void prepare() {
        prepare("rmi", 1099, "");
    }

    @Test
    @Ignore
    public void test_on_stress() throws IOException, InterruptedException {
        test_on_stress(Strings.repeat("a", 1024), 32);
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
