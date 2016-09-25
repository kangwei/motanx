package com.opensoft.motanx.rpc.protocol;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.protocol.hessian.HessianProtocol;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kangwei on 2016/9/25.
 */
public class ProtocolBaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProtocolBaseTest.class);
    protected URL url;
    protected Provider<DemoService> provider;
    protected DefaultRequest request;
    protected Protocol protocol = new HessianProtocol();

    public void prepare(String protocolName, int port, String server) {
        url = new URL(protocolName, "localhost", port, "com.opensoft.motanx.demo.DemoService");
        url.setParameter(UrlConstants.server.getName(), server);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.DemoService");
        request.setMethodName("hello");
    }

    public void after() {
        protocol.destroy();
    }

    public void test_on_stress(String requestObj, int threadNum) throws IOException, InterruptedException {
        protocol.export(provider);
        request.setMethodName("hello");
        request.setParameterTypes(new Class[]{String.class});
        request.setArgs(new Object[]{requestObj});
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            final int index = i;
            new Thread() {
                @Override
                public void run() {
                    Provider<DemoService> refer = protocol.refer(DemoService.class, url);
                    Response response = refer.invoke(request);
                    log.info("thread num {} complete, time loss : {}", index, response.getProcessTime());
                    countDownLatch.countDown();
                }
            }.start();
        }
        countDownLatch.await();
    }

    public Response test_on_single_string() {
        protocol.export(provider);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        return refer.invoke(request);
    }

    public Response test_on_wrong_request() {
        url.setPort(9001);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Exporter<DemoService> exporter = protocol.export(provider);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        request.setMethodName("sayHello");
        return refer.invoke(request);
    }

    public Response test_on_pojo() {
        url.setPort(9002);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Exporter<DemoService> exporter = protocol.export(provider);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        request.setMethodName("pojo");
        DemoPojo pojo = new DemoPojo();
        pojo.setName("haha");
        pojo.setAge(19);
        request.setParameterTypes(new Class[]{DemoPojo.class});
        request.setArgs(new Object[]{pojo});
        return refer.invoke(request);
    }
}
