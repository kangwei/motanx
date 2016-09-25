package com.opensoft.motanx.rpc;

import java.util.Map;

/**
 * RPC响应
 * Created by kangwei on 2016/6/26.
 */
public interface Response {
    Object getValue();

    String getVersion();

    void setVersion(String version);

    Map<String, String> getAttachment();

    void setAttachment(Map<String, String> attachment);

    void setAttachment(String key, String value);

    String getRequestId();

    Exception getException();

    boolean hasException();

    long getProcessTime();
}
