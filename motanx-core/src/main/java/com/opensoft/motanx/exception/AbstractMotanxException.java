package com.opensoft.motanx.exception;

/**
 * 抽象的框架异常
 * Created by kangwei on 2016/6/26.
 */
public abstract class AbstractMotanxException extends Exception {
    protected int code = ErrorCode.undefined.getCode();

    public AbstractMotanxException() {
    }

    public AbstractMotanxException(String message) {
        super(message);
    }

    public AbstractMotanxException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractMotanxException(Throwable cause) {
        super(cause);
    }

    public AbstractMotanxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractMotanxException(int code) {
        this.code = code;
    }

    public AbstractMotanxException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AbstractMotanxException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AbstractMotanxException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isBizException() {
        return false;
    }
}
