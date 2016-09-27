package com.opensoft.motanx.remote.transport.support;

import com.opensoft.motanx.core.URL;

/**
 * Created by kangwei on 2016/9/26.
 */
public abstract class AbstractPooledClient extends AbstractClient {
    public AbstractPooledClient(URL url) {
        super(url);
    }
}
