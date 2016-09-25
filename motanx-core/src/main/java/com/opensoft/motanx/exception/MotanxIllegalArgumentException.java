package com.opensoft.motanx.exception;

/**
 * 框架参数异常
 * Created by kangwei on 2016/8/24.
 */
public class MotanxIllegalArgumentException extends AbstractMotanxRuntimeException {
    public MotanxIllegalArgumentException() {
    }

    public MotanxIllegalArgumentException(String message) {
        super(message);
    }

    public MotanxIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotanxIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public MotanxIllegalArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MotanxIllegalArgumentException(int code) {
        super(code);
    }

    public MotanxIllegalArgumentException(int code, String message) {
        super(code, message);
    }

    public MotanxIllegalArgumentException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public MotanxIllegalArgumentException(int code, Throwable cause) {
        super(code, cause);
    }
}
