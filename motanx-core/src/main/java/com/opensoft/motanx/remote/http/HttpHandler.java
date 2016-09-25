package com.opensoft.motanx.remote.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kangwei on 2016/9/9.
 */
public interface HttpHandler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
