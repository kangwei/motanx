package com.opensoft.motanx.remote.http;

import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.core.URL;

/**
 * Created by kangwei on 2016/9/11.
 */
@Spi
public interface HttpBinder {
    HttpServer bind(URL url);
}
