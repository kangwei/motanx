package com.opensoft.motanx.remote.http;

import com.opensoft.motanx.rpc.Node;

import javax.servlet.Servlet;

/**
 * Created by kangwei on 2016/9/9.
 */
public interface HttpServer extends Node {
    void addServlet(Servlet servlet, String path);
}
