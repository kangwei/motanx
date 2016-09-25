package com.opensoft.motanx.exception;

/**
 * Created by kangwei on 2016/8/24.
 */
public enum ErrorCode {
    illegalArgument(100000),
    undefined(900001);


    ErrorCode(int i) {
        this.code = i;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
