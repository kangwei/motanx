package com.opensoft.motanx.logger;

import com.opensoft.motanx.core.Spi;

/**
 * 日志接口，对应多种日志输出
 * Created by kangwei on 2016/8/24.
 */
@Spi
public interface Logger {
    /**
     * 输出跟踪信息
     *
     * @param msg 信息内容
     */
    public void trace(String msg);

    /**
     * 输出跟踪信息
     *
     * @param msg     信息内容
     * @param objects 参数
     */
    public void trace(String msg, Object... objects);

    /**
     * 输出跟踪信息
     *
     * @param msg 信息内容
     * @param e   异常信息
     */
    public void trace(String msg, Throwable e);

    /**
     * 输出调试信息
     *
     * @param msg 信息内容
     */
    public void debug(String msg);

    /**
     * 输出调试信息
     *
     * @param e 异常信息
     */
    public void debug(String msg, Object... objects);

    /**
     * 输出调试信息
     *
     * @param msg 信息内容
     * @param e   异常信息
     */
    public void debug(String msg, Throwable e);

    /**
     * 输出普通信息
     *
     * @param msg 信息内容
     */
    public void info(String msg);

    /**
     * 输出普通信息
     *
     * @param e 异常信息
     */
    public void info(String msg, Object... objects);

    /**
     * 输出普通信息
     *
     * @param msg 信息内容
     * @param e   异常信息
     */
    public void info(String msg, Throwable e);

    /**
     * 输出警告信息
     *
     * @param msg 信息内容
     */
    public void warn(String msg);

    /**
     * 输出警告信息
     *
     * @param e 异常信息
     */
    public void warn(String msg, Object... objects);

    /**
     * 输出警告信息
     *
     * @param msg 信息内容
     * @param e   异常信息
     */
    public void warn(String msg, Throwable e);

    /**
     * 输出错误信息
     *
     * @param msg 信息内容
     */
    public void error(String msg);

    /**
     * 输出错误信息
     *
     * @param e 异常信息
     */
    public void error(String msg, Object... objects);

    /**
     * 输出错误信息
     *
     * @param msg 信息内容
     * @param e   异常信息
     */
    public void error(String msg, Throwable e);

    /**
     * 跟踪信息是否开启
     *
     * @return 是否开启
     */
    public boolean isTraceEnabled();

    /**
     * 调试信息是否开启
     *
     * @return 是否开启
     */
    public boolean isDebugEnabled();

    /**
     * 普通信息是否开启
     *
     * @return 是否开启
     */
    public boolean isInfoEnabled();

    /**
     * 警告信息是否开启
     *
     * @return 是否开启
     */
    public boolean isWarnEnabled();

    /**
     * 错误信息是否开启
     *
     * @return 是否开启
     */
    public boolean isErrorEnabled();
}
