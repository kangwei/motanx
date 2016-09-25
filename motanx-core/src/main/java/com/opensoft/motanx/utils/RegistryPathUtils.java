package com.opensoft.motanx.utils;

import com.opensoft.motanx.core.MotanxConstants;
import com.opensoft.motanx.core.URL;
import com.opensoft.motanx.core.UrlConstants;
import com.opensoft.motanx.exception.MotanxFrameworkException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by kangwei on 2016/9/4.
 */
public class RegistryPathUtils {

    public static String toNodeParentPath(URL url) {
        String nodeType = MotanxConstants.PATH_SEPARATOR + url.getParameter(UrlConstants.nodeType.getName(), UrlConstants.nodeType.getString());
        return nodeType + MotanxConstants.PATH_SEPARATOR + url.getPath();
    }

    public static String toUrlPath(URL url) {
        try {
            return toNodeParentPath(url) + MotanxConstants.PATH_SEPARATOR + URLEncoder.encode(url.getUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MotanxFrameworkException("encode url error", e);
        }
    }
}
