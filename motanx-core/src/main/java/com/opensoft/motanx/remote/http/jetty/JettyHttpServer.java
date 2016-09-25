package com.opensoft.motanx.remote.http.jetty;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.remote.http.HttpHandler;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.remote.http.support.AbstractHttpServer;
import com.opensoft.motanx.remote.http.support.DispatcherServlet;
import com.opensoft.motanx.rpc.protocol.rest.RestApplication;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;

import javax.servlet.Servlet;

/**
 * Created by kangwei on 2016/9/11.
 */
public class JettyHttpServer extends AbstractHttpServer implements HttpServer {
    public static final Logger logger = LoggerFactory.getLogger(JettyHttpServer.class);

    private Server server;

    private Context root;

    public JettyHttpServer(URL url) {
        super(url);
        init();
    }

    @Override
    protected void doAddServlet(Servlet servlet, String path) {
        ServletHolder holder = new ServletHolder(servlet);
        root.addServlet(holder, path);
    }

    @Override
    protected boolean doInit() {
        int threadNum = url.getIntParameter(UrlConstants.threadNum.getName(), UrlConstants.threadNum.getInt());
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
        queuedThreadPool.setDaemon(true);
        queuedThreadPool.setMaxThreads(threadNum);
        queuedThreadPool.setMinThreads(threadNum);

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setHost(url.getHost());
        connector.setPort(url.getPort());

        server = new Server();
        server.setThreadPool(queuedThreadPool);
        server.addConnector(connector);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);
        root = new Context(contexts, "/", Context.SESSIONS);

        try {
            server.start();
        } catch (Exception e) {
            logger.error("failed to start jetty server on port {}", url.getPort(), e);
            throw new MotanxFrameworkException("failed to start jetty server on port" + url.getPort());
        }
        return true;
    }

    @Override
    protected boolean doDestroy() {
        if (server != null) {
            try {
                if (server.isRunning()) {
                    server.stop();
                    server.destroy();
                }
            } catch (Exception e) {
                logger.error("failed to destroy jetty server on port {}", url.getPort(), e);
            }
        }
        return true;
    }
}
