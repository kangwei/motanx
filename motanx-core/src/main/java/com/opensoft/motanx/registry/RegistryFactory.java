package com.opensoft.motanx.registry;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;

/**
 * 注册中心工厂
 * Created by kangwei on 2016/9/2.
 */
@Spi
public interface RegistryFactory {
    /**
     * 获取注册中心
     *
     * @param url
     * @return
     */
    Registry getRegistry(URL url);
}
