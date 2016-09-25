package com.opensoft.motanx.exception;

/**
 * 框架RPC运行时异常
 * Created by kangwei on 2016/8/24.
 */
public class MotanxFrameworkException extends AbstractMotanxRuntimeException {
    public MotanxFrameworkException() {
    }

    public MotanxFrameworkException(String message) {
        super(message);
    }

    public MotanxFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotanxFrameworkException(Throwable cause) {
        super(cause);
    }

    public MotanxFrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MotanxFrameworkException(int code) {
        super(code);
    }

    public MotanxFrameworkException(int code, String message) {
        super(code, message);
    }

    public MotanxFrameworkException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public MotanxFrameworkException(int code, Throwable cause) {
        super(code, cause);
    }
}
