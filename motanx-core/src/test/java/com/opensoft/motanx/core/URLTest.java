package com.opensoft.motanx.core;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by kangwei on 2016/8/24.
 */
public class URLTest {
    public static final Logger log = LoggerFactory.getLogger(URLTest.class);
    private URL url;

    @Before
    public void buildURL() {
        Map<String, String> params = Maps.newHashMap();
        params.put("version", "1.0");
        params.put("timeout", "2000");
        url = new URL("rmi", "localhost", 8080, "com.opensoft.test.Test", params);
    }

    @Test
    public void should_equals_when_given_correct_url() {
        String uri = url.getUri();
        log.debug("uri = {}", uri);
        String identifyUrl = url.getIdentifyUrl();
        log.debug("identifyUrl = {}", identifyUrl);
        String expectUri = "rmi://localhost:8080/com.opensoft.test.Test";
        Assert.assertEquals(uri, expectUri);
        URL expectUrl = URL.valueOf(identifyUrl);
        Assert.assertEquals(url, expectUrl);
        Assert.assertTrue(url.equals(expectUrl));
    }
}
