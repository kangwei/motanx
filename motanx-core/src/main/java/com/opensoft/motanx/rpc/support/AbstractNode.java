package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.exception.MotanxRpcException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.rpc.Node;

/**
 * 节点的抽象实现，服务提供者，服务调用者都为节点
 * Created by kangwei on 2016/8/25.
 */
public abstract class AbstractNode implements Node {
    public static final Logger logger = LoggerFactory.getLogger(AbstractNode.class);

    protected URL url;
    private volatile boolean init = false;
    private volatile boolean alive = false;

    public AbstractNode(URL url) {
        this.url = url;
    }

    @Override
    public void init() {
        if (init) {
            logger.warn("Node " + url.getUri() + " is already init");
        }

        boolean isSuccess = doInit();

        if (isSuccess) {
            logger.info("Node " + url.getUri() + " init success");
            init = true;
            alive = true;
        } else {
            logger.error("Node " + url.getUri() + "init fail");
            throw new MotanxRpcException("Node " + url.getUri() + "init fail");
        }
    }

    protected abstract boolean doInit();

    @Override
    public void destroy() {
        boolean destroySuccess = doDestroy();
        if (destroySuccess) {
            logger.info("Node " + url.getUri() + " destroy success");
            init = false;
            alive = false;
        } else {
            logger.error("Node " + url.getUri() + "init fail");
        }

    }

    protected abstract boolean doDestroy();

    @Override
    public boolean isAvailable() {
        return alive;
    }

    @Override
    public String desc() {
        return url.toString();
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
