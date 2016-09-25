package com.opensoft.motanx.remote.http.jetty;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.remote.http.support.AbstractHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.Servlet;

/**
 * Created by kangwei on 2016/9/11.
 */
public class JettyHttpServer extends AbstractHttpServer implements HttpServer {
    public static final Logger logger = LoggerFactory.getLogger(JettyHttpServer.class);

    private Server server;

    private ServletContextHandler context;

    public JettyHttpServer(URL url) {
        super(url);
        init();
    }

    @Override
    protected void doAddServlet(Servlet servlet, String path) {
        ServletHolder holder = new ServletHolder(servlet);
        context.addServlet(holder, path);
    }

    @Override
    protected boolean doInit() {
        int threadNum = url.getIntParameter(UrlConstants.threadNum.getName(), UrlConstants.threadNum.getInt());
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setDaemon(true);
        threadPool.setMaxThreads(threadNum);
        threadPool.setMinThreads(threadNum);
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setHost(url.getHost());
        connector.setPort(url.getPort());
        connector.setThreadPool(threadPool);
        connector.setRequestBufferSize(8192);
        connector.setReuseAddress(true);

        server.addConnector(connector);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

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
