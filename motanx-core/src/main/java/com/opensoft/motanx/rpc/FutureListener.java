package com.opensoft.motanx.rpc;

/**
 * Created by kangwei on 2016/9/27.
 */
public interface FutureListener {
    /**
     * 完成时触发
     *
     * @param future
     */
    void onDone(Future future);
}
