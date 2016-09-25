package com.opensoft.motanx.logger;

import com.opensoft.motanx.logger.slf4j.Slf4jLoggerAdapter;

/**
 * Logger工厂，用于获取Logger的实例
 * Created by kangwei on 2016/8/24.
 */
public final class LoggerFactory {
    private LoggerFactory(){}

    public static Logger getLogger(Class cls) {
        String logName = "slf4j";
        if ("slf4j".equals(logName)) {
            return new Slf4jLoggerAdapter().getLogger(cls);
        }

        return new Slf4jLoggerAdapter().getLogger(cls);
    }

    public static Logger getLogger(String name) {
        String logName = "slf4j";
        if ("slf4j".equals(logName)) {
            return new Slf4jLoggerAdapter().getLogger(name);
        }

        return new Slf4jLoggerAdapter().getLogger(name);
    }
}
