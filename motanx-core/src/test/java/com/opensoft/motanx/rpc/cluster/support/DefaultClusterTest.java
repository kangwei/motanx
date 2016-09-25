package com.opensoft.motanx.rpc.cluster.support;

import com.google.common.collect.Lists;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.cluster.Cluster;
import com.opensoft.motanx.rpc.cluster.hastrategy.FailfastHaStrategy;
import com.opensoft.motanx.rpc.cluster.loadbalance.RandomLoadBalance;
import com.opensoft.motanx.rpc.protocol.rmi.RmiProtocol;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import com.opensoft.motanx.rpc.support.DefaultRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kangwei on 2016/9/9.
 */
public class DefaultClusterTest<T> {
    List<Provider<DemoService>> referers = Lists.newArrayList();
    URL url = new URL("rmi", "localhost", 8080, "com.opensoft.motanx.demo.DemoService");
    private DefaultRequest request;

    @Before
    public void prepare() {
        Provider provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Protocol protocol = new RmiProtocol();
        protocol.export(provider);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        refer.init();
        referers.add(refer);
        request = new DefaultRequest();
        request.setInterfaceName("com.opensoft.motanx.demo.DemoService");
        request.setMethodName("hello");
    }

    @Test
    public void test_invoke() {
        Cluster<DemoService> cluster = new DefaultCluster<>();
        cluster.setHaStrategy(new FailfastHaStrategy());
        cluster.setLoadBalance(new RandomLoadBalance());
        cluster.onRefresh(referers);
        Response response = cluster.invoke(request);
        System.out.println(response);
    }
}
