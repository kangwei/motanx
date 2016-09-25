package com.opensoft.motanx.registry.local;

import com.opensoft.motanx.core.Scope;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.registry.Registry;
import com.opensoft.motanx.registry.RegistryFactory;
import com.opensoft.motanx.registry.support.AbstractRegistryFactory;

/**
 * 本地注册中心
 * Created by kangwei on 2016/9/4.
 */
@Spi(name = "local", scope = Scope.SINGLETON)
public class LocalRegistryFactory extends AbstractRegistryFactory implements RegistryFactory {
    @Override
    protected Registry createRegistry(URL url) {
        return new LocalRegistry(url);
    }
}
