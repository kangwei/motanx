package com.opensoft.motanx.rpc;

import java.util.Map;

/**
 * RPC请求
 * Created by kangwei on 2016/6/26.
 */
public interface Request {
    String getInterfaceName();

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArgs();

    int getTimeOut();

    void setTimeOut(int timeOut);

    int getRetries();

    void setRetries(int retries);

    String getVersion();

    void setVersion(String version);

    String getRequestId();

    Map<String, String> getAttachment();

    void setAttachment(Map<String, String> attachment);

    void setAttachment(String key, String value);
}
