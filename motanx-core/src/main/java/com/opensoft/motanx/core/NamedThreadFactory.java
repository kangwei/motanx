package com.opensoft.motanx.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义命名的线程工厂
 * Created by kangwei on 2016/9/3.
 */
public class NamedThreadFactory implements ThreadFactory {
    public static final AtomicInteger POOL_SEQ = new AtomicInteger(0);
    private String prefix;
    private boolean deamon = false;
    private AtomicInteger threadNum = new AtomicInteger(0);

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.addAndGet(1));
    }

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public NamedThreadFactory(String prefix, boolean deamon) {
        this.prefix = prefix;
        this.deamon = deamon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(prefix + "-" + threadNum.addAndGet(1) + "-thread");
        thread.setDaemon(deamon);
        return thread;
    }
}
