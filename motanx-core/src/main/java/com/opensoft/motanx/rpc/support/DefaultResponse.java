package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.core.MotanxConstants;
import com.opensoft.motanx.rpc.Response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认响应实现
 * Created by kangwei on 2016/6/26.
 */
public class DefaultResponse implements Response, Serializable {
    protected Object value;
    protected Exception exception;
    protected String version = MotanxConstants.DEFAULT_VERSION;
    protected String requestId;
    protected long processTime;
    protected Map<String, String> attachment;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
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

    @Override
    public String toString() {
        return "Response{" +
                "value=" + value +
                ", version='" + version + '\'' +
                ", exception=" + exception +
                ", requestId='" + requestId + '\'' +
                ", processTime=" + processTime +
                '}';
    }
}
