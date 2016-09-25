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
import com.opensoft.motanx.rpc.protocol.ProtocolBaseTest;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import org.junit.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kangwei on 2016/9/11.
 */
public class HessianProtocolTest extends ProtocolBaseTest {

    @Before
    public void prepare() {
        prepare("hessian", 9000, "jetty");
    }

    @After
    public void after() {
        protocol.destroy();
    }

    @Test
    @Ignore
    public void test_on_stress() throws IOException, InterruptedException {
        test_on_stress(Strings.repeat("a", 1024), 20);
    }

    @Test
    public void test_hessian_protocol() {
        Response response = test_on_single_string();
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getValue(), "hello rpc");
    }

    @Test
    public void test_hessian_protocol_on_wrong_request() {
        Response response = test_on_wrong_request();
        Assert.assertNotNull(response.getException());
    }

    @Test
    public void test_hessian_protocol_on_pojo() {
        Response response = test_on_pojo();
        Assert.assertEquals(response.getValue().getClass(), DemoPojo.class);
        DemoPojo demoPojo = (DemoPojo) response.getValue();
        Assert.assertEquals(demoPojo.getName(), "haha");
        Assert.assertSame(demoPojo.getAge(), 19);
    }
}
