package com.opensoft.motanx.rpc.protocol.injvm;

import com.google.common.base.Strings;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.protocol.ProtocolBaseTest;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import org.junit.*;

import java.io.IOException;

/**
 * Created by kangwei on 2016/9/24.
 */
public class InjvmProtocolTest extends ProtocolBaseTest {
    private static final Logger log = LoggerFactory.getLogger(InjvmProtocolTest.class);
    @Before
    public void prepare() {
        prepare("injvm", 0, "");
    }

    @After
    public void after() {
        super.after();
    }

    @Test
    @Ignore
    public void test_on_stress() throws IOException, InterruptedException {
        test_on_stress(Strings.repeat("a", 1024), 32);
    }

    @Test
    public void test_hessian_protocol() {
        Response response = test_on_single_string();
        log.info(response.toString());
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getValue(), "hello rpc");
        Response response1 = test_on_another_provider();
        log.info(response1.toString());
        Assert.assertNotNull(response1);
        Assert.assertEquals(response1.getValue(), "world");
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
