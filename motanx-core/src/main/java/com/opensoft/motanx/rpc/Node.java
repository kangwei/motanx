package com.opensoft.motanx.rpc;

import com.opensoft.motanx.core.URL;

/**
 * 虚拟节点抽象接口
 * Created by kangwei on 2016/6/26.
 */
public interface Node {
    /**
     * 节点初始化
     */
    void init();

    /**
     * 节点销毁
     */
    void destroy();

    /**
     * 节点是否有效
     *
     * @return true-有效，false-无线
     */
    boolean isAvailable();

    String desc();

    /**
     * 节点URL描述
     *
     * @return URL
     */
    URL getUrl();
}
