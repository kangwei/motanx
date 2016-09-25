package com.opensoft.motanx.logger;

import com.opensoft.motanx.core.Spi;

/**
 * 日志输出器供给器
 * Created by kangwei on 2016/8/24.
 */
@Spi
public interface LoggerAdapter {
    /**
     * 获取日志输出器
     *
     * @param key 分类键
     * @return 日志输出器, 后验条件: 不返回null.
     */
    Logger getLogger(Class<?> key);

    /**
     * 获取日志输出器
     *
     * @param key 分类键
     * @return 日志输出器, 后验条件: 不返回null.
     */
    Logger getLogger(String key);

    /**
     * 设置输出等级
     *
     * @param level 输出等级
     */
    void setLevel(Level level);

    /**
     * 获取当前日志等级
     *
     * @return 当前日志等级
     */
    Level getLevel();
}
