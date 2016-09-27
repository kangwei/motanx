package com.opensoft.motanx.remote.transport.support;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.remote.transport.Client;

/**
 * Created by kangwei on 2016/9/26.
 */
public abstract class AbstractClient extends AbstractEndPoint implements Client {
    public AbstractClient(URL url) {
        super(url);
    }
}
