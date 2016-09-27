package com.opensoft.motanx.rpc.support;

import com.google.common.collect.Sets;
import com.opensoft.motanx.exception.MotanxRpcException;
import com.opensoft.motanx.rpc.Future;
import com.opensoft.motanx.rpc.FutureListener;
import com.opensoft.motanx.rpc.Response;
import com.opensoft.motanx.rpc.RpcContext;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 异步响应
 * Created by kangwei on 2016/9/27.
 */
public class FutureResponse extends DefaultResponse implements Response, Future, Serializable {
    //有可能多线程获取结果，所以需要是volatile
    private volatile FutureState futureState = FutureState.doing;
    private volatile boolean isSuccess = false;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private Set<FutureListener> listeners = Sets.newConcurrentHashSet();
    private long timeout = 3 * 1000L;

    @Override
    public boolean cancel() {
        if (!isDoing()) {
            return false;
        }

        return cancel(new MotanxRpcException("task cancel"));
    }

    private boolean cancel(Exception e) {
        exception = e;
        futureState = FutureState.cancel;
        notifyListeners();
        countDownLatch.countDown();
        return true;
    }

    @Override
    public boolean isCancelled() {
        return FutureState.cancel.equals(futureState);
    }

    @Override
    public boolean isDone() {
        return FutureState.done.equals(futureState);
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public void addListener(FutureListener listener) {
        if (listener == null) {
            return;
        }
        if (!isDoing()) {
            return;
        }
        listeners.add(listener);
    }

    @Override
    public Object getValue() {
        return getValue(timeout);
    }

    @Override
    public Object getValue(long timeout) {
        if (isDone()) {
            return getValueOrThrowable();
        }
        boolean acquire;
        try {
            acquire = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if (acquire) {
                return getValueOrThrowable();
            }
        } catch (InterruptedException e) {
            cancel(e);
        }
        cancel(new MotanxRpcException("timeout cancel"));
        return getValueOrThrowable();
    }

    public void onSuccess(Object value) {
        isSuccess = true;
        this.value = value;
        done();
    }

    private void done() {
        this.futureState = FutureState.done;
        countDownLatch.countDown();
        notifyListeners();
    }

    public void onFailure(Exception e) {
        this.exception = e;
        done();
    }

    private void notifyListeners() {
        for (FutureListener listener : listeners) {
            listener.onDone(this);
        }
    }

    public boolean isDoing() {
        return FutureState.doing.equals(futureState);
    }

    public Object getValueOrThrowable() {
        if (exception != null) {
            throw (exception instanceof RuntimeException) ? (RuntimeException) exception : new MotanxRpcException(exception);
        }
        return value;
    }
}
