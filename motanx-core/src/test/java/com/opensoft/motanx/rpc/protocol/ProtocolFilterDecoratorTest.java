package com.opensoft.motanx.rpc.protocol;

import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.protocol.rmi.RmiProtocol;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kangwei on 2016/9/24.
 */
public class ProtocolFilterDecoratorTest {
    private URL url;
    private Provider<DemoService> provider;
    private DefaultRequest request;

    @Before
    public void prepare() {
        url = new URL("rmi", "localhost", 1099, "com.opensoft.motanx.demo.DemoService");
        url.setParameter(UrlConstants.filters.getName(), "accesslog");
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.DemoService");
        request.setMethodName("hello");
    }

    @Test
    public void test_rmi_protocol() {
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(url.getProtocol());
        protocol = new ProtocolFilterDecorator(protocol);
        Exporter<DemoService> exporter = protocol.export(provider);
        Assert.assertNotNull(exporter);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        Response response = refer.invoke(request);
        System.out.println(response.toString());
        protocol.destroy();
    }
}
