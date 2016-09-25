package com.opensoft.motanx.exception;

/**
 * 框架RPC运行时异常
 * Created by kangwei on 2016/8/24.
 */
public class MotanxRpcException extends AbstractMotanxRuntimeException {
    public MotanxRpcException() {
    }

    public MotanxRpcException(String message) {
        super(message);
    }

    public MotanxRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotanxRpcException(Throwable cause) {
        super(cause);
    }

    public MotanxRpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MotanxRpcException(int code) {
        super(code);
    }

    public MotanxRpcException(int code, String message) {
        super(code, message);
    }

    public MotanxRpcException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public MotanxRpcException(int code, Throwable cause) {
        super(code, cause);
    }
}
