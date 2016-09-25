package com.opensoft.motanx.exception;

/**
 * 框架业务异常
 * Created by kangwei on 2016/6/26.
 */
public class MotanxBizException extends MotanxException {
    public MotanxBizException() {
    }

    public MotanxBizException(String message) {
        super(message);
    }

    public MotanxBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotanxBizException(Throwable cause) {
        super(cause);
    }

    public MotanxBizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MotanxBizException(int code) {
        super(code);
    }

    public MotanxBizException(int code, String message) {
        super(code, message);
    }

    public MotanxBizException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public MotanxBizException(int code, Throwable cause) {
        super(code, cause);
    }

    @Override
    public boolean isBizException() {
        return true;
    }
}
