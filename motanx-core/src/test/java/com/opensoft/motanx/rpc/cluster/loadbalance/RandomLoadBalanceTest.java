package com.opensoft.motanx.rpc.cluster.loadbalance;

import com.google.common.collect.Lists;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.cluster.LoadBalance;
import com.opensoft.motanx.rpc.support.DefaultProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kangwei on 2016/9/9.
 */
public class RandomLoadBalanceTest<T> {
    LoadBalance loadBalance = new RandomLoadBalance();
    List<Provider<T>> referers = Lists.newArrayList();
    List<Provider<T>> referers1 = Lists.newArrayList();
    List<Provider<T>> referers2 = Lists.newArrayList();
    List<Provider<T>> empty = Lists.newArrayList();

    @Before
    public void prepare() {
        URL url = new URL("rmi", "localhost", 9000, "test");

        Provider referer = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        referers.add(referer);
        Provider referer1 = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        referer1.init();
        referers1.add(referer1);
        referers2.add(referer1);
        Provider referer2 = new DefaultProvider<>(DemoService.class, url, new DemoServiceImpl());
        referer2.init();
        referers2.add(referer2);
    }

    @Test
    public void test_on_empty() {
        try {
            Provider select = loadBalance.select(empty);
        } catch (Exception e) {
            Assert.assertEquals("no provider exists", e.getMessage());
        }

    }

    @Test
    public void test_on_not_available() {
        try {
            Provider select = loadBalance.select(referers);
        } catch (Exception e) {
            Assert.assertEquals("no available provider", e.getMessage());
        }
    }

    @Test
    public void test_on_only_one() {
        Provider referer = loadBalance.select(referers1);
        Assert.assertNotNull(referer);
    }

    @Test
    public void test_on_two_refer() {
        Provider referer = null;
        for (int i = 0; i < 10; i++) {
            referer = loadBalance.select(referers2);
            Assert.assertNotNull(referer);
        }

    }
}
