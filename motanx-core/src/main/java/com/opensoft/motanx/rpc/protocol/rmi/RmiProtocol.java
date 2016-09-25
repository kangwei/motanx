package com.opensoft.motanx.rpc.protocol.rmi;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.proxy.ProxyFactory;
import com.opensoft.motanx.proxy.jdk.JdkProxyFactory;
import com.opensoft.motanx.rpc.Exporter;
import com.opensoft.motanx.rpc.Protocol;
import com.opensoft.motanx.rpc.Provider;
import com.opensoft.motanx.rpc.protocol.AbstractProtocol;
import com.opensoft.motanx.rpc.support.AbstractExporter;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;

/**
 * RMI protocol实现
 * Created by kangwei on 2016/8/27.
 */
@Spi(name = "rmi")
public class RmiProtocol extends AbstractProtocol implements Protocol {
    public static final Logger logger = LoggerFactory.getLogger(RmiProtocol.class);

    private int defaultPort = 1099;

    private Exporter exporter;
    @Override
    protected <T> Exporter<T> doExport(Provider<T> provider) {
        final RmiServiceExporter serviceExporter = new RmiServiceExporter();
        URL url = provider.getUrl();
        serviceExporter.setRegistryPort(url.getPort());
        serviceExporter.setServiceInterface(provider.getInterface());
        serviceExporter.setService(provider.getImpl());
        serviceExporter.setServiceName(url.getPath());
        try {
            serviceExporter.afterPropertiesSet();
        } catch (RemoteException e) {
            throw new MotanxFrameworkException(e.getMessage(), e);
        }
        exporter = new AbstractExporter<T>(provider) {
            @Override
            public void unexport() {
                try {
                    serviceExporter.destroy();
                } catch (RemoteException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        };

        return exporter;
    }

    @Override
    protected <T> T doRefer(Class<T> type, URL url) {
        RmiProxyFactoryBean proxyFactoryBean = new RmiProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(type);
        proxyFactoryBean.setCacheStub(true);
        proxyFactoryBean.setServiceUrl(url.getUri());
        proxyFactoryBean.setLookupStubOnStartup(true);
        proxyFactoryBean.setRefreshStubOnConnectFailure(true);
        proxyFactoryBean.afterPropertiesSet();

        return  (T) proxyFactoryBean.getObject();
    }

    @Override
    public int getDefaultPort() {
        return defaultPort;
    }
}
