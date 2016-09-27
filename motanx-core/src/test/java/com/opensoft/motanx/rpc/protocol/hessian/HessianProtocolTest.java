package com.opensoft.motanx.rpc.protocol.hessian;

import com.google.common.base.Strings;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.protocol.ProtocolBaseTest;
import org.junit.*;

import java.io.IOException;

/**
 * Created by kangwei on 2016/9/11.
 */
public class HessianProtocolTest extends ProtocolBaseTest {
    private static final Logger log = LoggerFactory.getLogger(HessianProtocolTest.class);
    @Before
    public void prepare() {
        prepare("hessian", 9000, "tomcat");
    }

    @After
    public void after() {
//        super.after();
    }

    @Test
    @Ignore
    public void test_on_stress() throws IOException, InterruptedException {
        test_on_stress(Strings.repeat("a", 1024), 32);
    }

    @Test
    public void test_hessian_protocol() {
        Response response = test_on_single_string();
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getValue(), "hello rpc");
        log.info(response.toString());
        Response response1 = test_on_another_provider();
        log.info(response1.toString());
        Assert.assertNotNull(response1);
        Assert.assertEquals(response1.getValue(), "world");
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
        log.info("get response:{}", response);
        Assert.assertEquals(demoPojo.getName(), "haha");
        Assert.assertSame(demoPojo.getAge(), 19);
    }
}
