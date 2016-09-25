package com.opensoft.motanx.registry.zookeeper;

import com.opensoft.motanx.core.Scope;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.registry.support.AbstractRegistryFactory;
import org.I0Itec.zkclient.ZkClient;

/**
 * zookeeper注册中心工厂
 * Created by kangwei on 2016/9/3.
 */
@Spi(name = "zookeeper", scope = Scope.SINGLETON)
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {
    private String host;
    private int port;

    public ZookeeperRegistryFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected Registry createRegistry(URL url) {
        ZkClient zkClient = new ZkClient(host + ":" + port);
        return new ZookeeperRegistry(url, zkClient);
    }
}
