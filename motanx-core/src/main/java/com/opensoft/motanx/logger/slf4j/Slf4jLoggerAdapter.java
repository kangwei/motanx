package com.opensoft.motanx.logger.slf4j;

import com.opensoft.motanx.core.Scope;
import com.opensoft.motanx.core.Spi;
import com.opensoft.motanx.logger.Level;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerAdapter;
import org.slf4j.LoggerFactory;

/**
 * Created by kangwei on 2016/8/24.
 */
@Spi(name = "slf4j", scope = Scope.SINGLETON)
public class Slf4jLoggerAdapter implements LoggerAdapter {

    public Logger getLogger(Class<?> key) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(key);
        return new Slf4jLogger(logger);
    }

    public Logger getLogger(String key) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(key);
        return new Slf4jLogger(logger);
    }

    public void setLevel(Level level) {

    }

    public Level getLevel() {
        return null;
    }
}
