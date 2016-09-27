package com.opensoft.motanx.rpc.protocol;

import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.demo.AnotherService;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.AnotherServiceImpl;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kangwei on 2016/9/25.
 */
public class ProtocolBaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProtocolBaseTest.class);
    protected URL url;
    protected URL anotherUrl;
    protected Provider<DemoService> provider;
    protected Provider<AnotherService> anotherProvider;
    protected DefaultRequest request;
    protected Protocol protocol;
    private static volatile AtomicInteger requestNum = new AtomicInteger(0);

    public void prepare(String protocolName, int port, String server) {
        url = new URL(protocolName, "localhost", port, "com.opensoft.motanx.demo.DemoService");
        url.setParameter(UrlConstants.server.getName(), server);
        provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        anotherUrl = new URL(protocolName, "localhost", port, "com.opensoft.motanx.demo.AnotherService");
        anotherProvider = new DefaultProvider<>(AnotherService.class, anotherUrl, new AnotherServiceImpl());
        request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.DemoService");
        request.setMethodName("hello");
        protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolName);
    }

    public void after() {
        protocol.destroy();
    }

    public void test_on_stress(String requestObj, int threadNum) throws IOException, InterruptedException {
        protocol.export(provider);
        request.setMethodName("hello");
        request.setParameterTypes(new Class[]{String.class});
        request.setArgs(new Object[]{requestObj});
        final Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (requestNum.incrementAndGet() < 10000) {
                        Response response = refer.invoke(request);
                        log.info("{} complete, time loss : {}", requestNum.get(), response.getProcessTime());
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        countDownLatch.await();
        long time = System.currentTimeMillis() - start;
        log.info("all done, time loss : {}, qps : {}", time, ((float)10000 / time * 1000L));
    }

    public Response test_on_single_string() {
        protocol.export(provider);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        return refer.invoke(request);
    }

    public Response test_on_another_provider() {
        protocol.export(anotherProvider);
        Provider<AnotherService> refer = protocol.refer(AnotherService.class, anotherUrl);
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
