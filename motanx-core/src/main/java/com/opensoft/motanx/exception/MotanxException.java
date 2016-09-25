package com.opensoft.motanx.exception;

/**
 * 框架受检异常
 * Created by kangwei on 2016/6/26.
 */
public class MotanxException extends AbstractMotanxException {
    public MotanxException() {
    }

    public MotanxException(String message) {
        super(message);
    }

    public MotanxException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotanxException(Throwable cause) {
        super(cause);
    }

    public MotanxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MotanxException(int code) {
        super(code);
    }

    public MotanxException(int code, String message) {
        super(code, message);
    }

    public MotanxException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public MotanxException(int code, Throwable cause) {
        super(code, cause);
    }
}
