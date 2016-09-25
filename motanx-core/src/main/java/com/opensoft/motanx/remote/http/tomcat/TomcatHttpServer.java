package com.opensoft.motanx.remote.http.tomcat;

import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.remote.http.HttpServer;
import com.opensoft.motanx.remote.http.support.AbstractHttpServer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.Servlet;

/**
 * embedded tomcat
 * //TODO:set tomcat config, for example : maxThread, minThread etc.
 * Created by kangwei on 2016/9/23.
 */
public class TomcatHttpServer extends AbstractHttpServer implements HttpServer {
    private final static String mWorkingDir = System.getProperty("java.io.tmpdir");
    private Tomcat tomcat;
    private Context ctx;

    public TomcatHttpServer(URL url) {
        super(url);
        init();
    }

    @Override
    protected void doAddServlet(Servlet servlet, String path) {
        String servletName = servlet.getClass().getSimpleName();
        tomcat.addServlet(ctx, servletName, servlet);
        ctx.addServletMapping(path, servletName);
    }

    @Override
    protected boolean doInit() {
        tomcat = new Tomcat();
        int threadNum = url.getIntParameter(UrlConstants.threadNum.getName(), UrlConstants.threadNum.getInt());
        tomcat.getConnector().setProtocol("org.apache.coyote.http11.Http11NioProtocol");
        tomcat.getConnector().setURIEncoding("UTF-8");
        tomcat.getConnector().setAttribute("maxThreads", threadNum);
        tomcat.getConnector().setPort(url.getPort());
//        tomcat.setPort(url.getPort());
        ctx = tomcat.addContext("/", mWorkingDir);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new MotanxFrameworkException("fail to start tomcat on port" + url.getPort(), e);
        }
        return true;
    }

    @Override
    protected boolean doDestroy() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new MotanxFrameworkException("fail to destroy tomcat on port" + url.getPort(), e);
        }
        return true;
    }
}
