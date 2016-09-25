package com.opensoft.motanx.rpc.support;

import com.opensoft.motanx.exception.MotanxIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kangwei on 2016/8/24.
 */
public class DefaultRequestTest {
    @Test
    public void should_equals_when_given_correct_parameter_desc() {
        DefaultRequest request = new DefaultRequest();
        request.setParameterTypes(new Class[]{String.class, Integer.class});
        Class<?>[] expect = new Class[]{String.class, Integer.class};
        Assert.assertArrayEquals(request.getParameterTypes(), expect);
    }

    @Test
    public void should_throw_exception_when_given_error_desc() {
        DefaultRequest request = new DefaultRequest();
        request.setParameterTypes(new Class[]{String.class, Long.class});
        try {
            Class<?>[] parametersType = request.getParameterTypes();
        } catch (Exception e) {
//            Assert.assertEquals(MotanxIllegalArgumentException.class, e.getClass());
//            e.printStackTrace();
        }
    }
}
