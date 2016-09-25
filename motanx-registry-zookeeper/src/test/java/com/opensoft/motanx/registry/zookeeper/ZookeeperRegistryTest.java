package com.opensoft.motanx.registry.zookeeper;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.registry.NotifyListener;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.registry.RegistryFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kangwei on 2016/9/3.
 */
public class ZookeeperRegistryTest {
    private Registry registry;
    RegistryFactory factory;
    URL regUrl;
    URL provider1;
    URL provider2;
    URL consumer;
    @Before
    public void prepare() {
        factory = new ZookeeperRegistryFactory("localhost", 2181);
        regUrl = new URL("registry", "localhost", 2181, "com.opensoft.motanx.registry.zookeeper.ZookeeperRegistry");
        provider1 = new URL("rmi", "localhost", 9000, "com.opensoft.motanx.demo.DemoService");
        provider2 = new URL("rmi", "localhost", 9001, "com.opensoft.motanx.demo.DemoService");
        consumer = new URL("referer", "localhost", 0, "com.opensoft.motanx.demo.DemoService");
    }

    @Test
    public void test_registry_on_normal() {
        registry = factory.getRegistry(regUrl);
        registry.register(provider1);
        registry.register(provider2);
        List<URL> lookup1 = registry.lookup(consumer);
        System.out.println(lookup1);
    }

    @Test
    public void test_subscribe_on_normal() {
        registry = factory.getRegistry(regUrl);
        registry.subscribe(consumer, new NotifyListener() {
            @Override
            public void notify(URL registryUrl, List<URL> urls) {
                System.out.println("-------->" + urls);
            }
        });

        registry.register(provider1);
        registry.register(provider2);
    }
}
