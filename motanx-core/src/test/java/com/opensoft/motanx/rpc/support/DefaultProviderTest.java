package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.rpc.Response;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kangwei on 2016/8/25.
 */
public class DefaultProviderTest {
    public static final Logger logger = LoggerFactory.getLogger(DefaultProviderTest.class);
    @Test
    public void test_error_on_method_not_found() {
        Request request = new DefaultRequest();
        DefaultProvider provider = new DefaultProvider(Request.class, new URL("rmi", "localhost", 80, "/test"), request);
        Response response = provider.invoke(request);
        logger.info(response.toString());
    }

    @Test
    public void test_error_on_bad_request() {
        DefaultRequest request = new DefaultRequest();
        request.setInterfaceName(Request.class.getName());
        request.setMethodName("setTimeOut");
        request.setParameterTypes(new Class[]{int.class});
        DefaultProvider provider = new DefaultProvider(Request.class, new URL("rmi", "localhost", 80, "/test"), request);
        Response response = provider.invoke(request);
        logger.info(response.toString());
    }
}
