package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.exception.MotanxRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by kangwei on 2016/9/27.
 */
public class FutureResponseTest {
    DefaultResponse normalResponse;
    DefaultResponse exceptionResponse;

    @Before
    public void prepare() {
        normalResponse = new DefaultResponse();
        normalResponse.setValue("test");
        normalResponse.setProcessTime(1L);

        exceptionResponse = new DefaultResponse();
        exceptionResponse.setException(new MotanxRpcException("exception"));
        exceptionResponse.setProcessTime(2L);
    }

    @Test
    public void test_on_normal() {
        FutureResponse response = new FutureResponse();
        response.onSuccess(normalResponse.getValue());
        Assert.assertEquals(response.getValue(), normalResponse.getValue());
    }

    @Test
    public void test_on_exception() {
        FutureResponse response = new FutureResponse();
        response.onFailure(exceptionResponse.getException());
        Assert.assertEquals(response.getException(), exceptionResponse.getException());
    }

    @Test
    public void test_on_timeout() throws InterruptedException, IOException {
        FutureResponse response = new FutureResponse();
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response.onSuccess(normalResponse.getValue());
            }
        }.start();
        try {
            response.getValue(49);
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), MotanxRpcException.class);
            Assert.assertEquals(e.getMessage(), "timeout cancel");
        }

    }
}
