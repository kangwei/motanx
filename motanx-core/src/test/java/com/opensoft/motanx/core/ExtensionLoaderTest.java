package com.opensoft.motanx.core;

import com.opensoft.motanx.logger.LoggerAdapter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kangwei on 2016/8/26.
 */
public class ExtensionLoaderTest {
    @Test
    public void test_get_extension_on_right_spi() {
        LoggerAdapter slf4j = ExtensionLoader.getExtensionLoader(LoggerAdapter.class).getExtension("slf4j");
        Assert.assertNotNull(slf4j);
    }

    @Test
    public void test_get_singleton_on_right_spi() {
        LoggerAdapter slf4j = ExtensionLoader.getExtensionLoader(LoggerAdapter.class).getExtension("slf4j");
        Assert.assertNotNull(slf4j);
        LoggerAdapter slf4j1 = ExtensionLoader.getExtensionLoader(LoggerAdapter.class).getExtension("slf4j");
        Assert.assertSame(slf4j, slf4j1);
    }

    @Test
    public void test_get_extension_error() {
        LoggerAdapter slf4j = null;
        try {
            slf4j = ExtensionLoader.getExtensionLoader(LoggerAdapter.class).getExtension("1111");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "extensionClass 1111 is not found");
        }

    }


}
