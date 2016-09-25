package com.opensoft.motanx.proxy.jdk;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.opensoft.motanx.core.ExtensionLoader;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.cluster.Cluster;
import com.opensoft.motanx.rpc.cluster.hastrategy.FailfastHaStrategy;
import com.opensoft.motanx.rpc.cluster.loadbalance.RandomLoadBalance;
import com.opensoft.motanx.rpc.cluster.support.DefaultCluster;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kangwei on 2016/9/9.
 */
public class JdkProxyFactoryTest {
    List<Provider<DemoService>> referers = Lists.newArrayList();
    URL url = new URL("rmi", "localhost", 8080, "com.opensoft.motanx.demo.DemoService");
    Cluster<DemoService> cluster = new DefaultCluster<>();

    @Before
    public void prepare() {
        Provider provider = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(url.getProtocol());
        protocol.export(provider);
        Provider<DemoService> refer = protocol.refer(DemoService.class, url);
        refer.init();
        referers.add(refer);
        cluster.setHaStrategy(new FailfastHaStrategy());
        cluster.setLoadBalance(new RandomLoadBalance());
        cluster.onRefresh(referers);
    }

    @Test
    public void test_get_proxy() {
        ProxyFactory proxyFactory = new JdkProxyFactory();
        String s = Strings.repeat("a", 2);
        DemoService proxy = proxyFactory.getProxy(DemoService.class, cluster);

        String hello = proxy.sayHello(s);
        Assert.assertEquals(hello, s);
    }
}
