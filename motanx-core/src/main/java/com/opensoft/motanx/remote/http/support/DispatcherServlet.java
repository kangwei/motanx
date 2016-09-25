package com.opensoft.motanx.remote.http.support;

import com.google.common.collect.Maps;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;
import com.opensoft.motanx.remote.http.HttpHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by kangwei on 2016/9/11.
 */
public class DispatcherServlet extends HttpServlet {
    public static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static ConcurrentMap<Integer, HttpHandler> handlers = Maps.newConcurrentMap();

    private static DispatcherServlet INSTANCE;

    public DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        if (INSTANCE == null) {
            synchronized (DispatcherServlet.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DispatcherServlet();
                }
            }
        }
        return INSTANCE;
    }

    public static void addHandler(Integer port, HttpHandler handler) {
        handlers.putIfAbsent(port, handler);
    }

    public static void removeHandler(Integer port) {
        handlers.remove(port);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int serverPort = req.getServerPort();
        HttpHandler handler = handlers.get(serverPort);
        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "service not found.");
        } else {
            handler.handle(req, resp);
        }
    }
}
