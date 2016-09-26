package com.opensoft.motanx.rpc.protocol.rest;

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
 * Created by kangwei on 2016/9/12.
 */
public class RestProtocolTest extends ProtocolBaseTest {
    private static final Logger log = LoggerFactory.getLogger(RestProtocolTest.class);
    @Before
    public void prepare() {
        prepare("rest", 9000, "tomcat");
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
    public void test_rest_protocol() throws IOException {
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
        Response response = test_on_pojo();
        Assert.assertEquals(response.getValue().getClass(), DemoPojo.class);
        DemoPojo demoPojo = (DemoPojo) response.getValue();
        log.info("get response:{}", response);
        Assert.assertEquals(demoPojo.getName(), "haha");
        Assert.assertSame(demoPojo.getAge(), 19);
    }
}
