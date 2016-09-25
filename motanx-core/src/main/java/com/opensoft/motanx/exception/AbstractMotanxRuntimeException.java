package com.opensoft.motanx.exception;

/**
 * 抽象的框架运行时异常
 * Created by kangwei on 2016/8/24.
 */
public class AbstractMotanxRuntimeException extends RuntimeException {
    protected int code = ErrorCode.undefined.getCode();

    public AbstractMotanxRuntimeException() {
    }

    public AbstractMotanxRuntimeException(String message) {
        super(message);
    }

    public AbstractMotanxRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractMotanxRuntimeException(Throwable cause) {
        super(cause);
    }

    public AbstractMotanxRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractMotanxRuntimeException(int code) {
        this.code = code;
    }

    public AbstractMotanxRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AbstractMotanxRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AbstractMotanxRuntimeException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
