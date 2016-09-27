package com.opensoft.motanx.remote.transport.support;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.remote.transport.Server;

/**
 * Created by kangwei on 2016/9/26.
 */
public abstract class AbstractServer extends AbstractEndPoint implements Server {
    public AbstractServer(URL url) {
        super(url);
    }
}
