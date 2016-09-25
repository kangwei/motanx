package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.exception.ErrorCode;
import com.opensoft.motanx.exception.MotanxIllegalArgumentException;
import com.opensoft.motanx.rpc.Request;
import com.opensoft.motanx.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认请求
 * Created by kangwei on 2016/6/26.
 */
public class DefaultRequest implements Request, Serializable {
    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
    private String requestId;
    private Map<String, String> attachment;
    private String version;
    private int timeOut;
    private int retries;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameterDesc() {
        return ReflectUtils.getParameterDesc(parameterTypes);
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, String> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, String> attachment) {
        this.attachment = attachment;
    }

    public void setAttachment(String key, String value) {
        if (this.attachment == null) {
            this.attachment = new HashMap<String, String>();
        }

        this.attachment.put(key, value);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public String toString() {
        return "DefaultRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parametersDesc='" + getParameterDesc() + '\'' +
                ", args=" + Arrays.toString(args) +
                ", requestId='" + requestId + '\'' +
                ", version='" + version + '\'' +
                ", attachment=" + attachment +
                '}';
    }
}
