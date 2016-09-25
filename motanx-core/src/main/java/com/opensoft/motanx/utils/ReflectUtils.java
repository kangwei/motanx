package com.opensoft.motanx.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 反射工具类
 * Created by kangwei on 2016/8/24.
 */
public class ReflectUtils {
    /**
     * 获取方法参数的描述
     *
     * @param parameterTypes 参数
     * @return 描述
     */
    public static String getParameterDesc(Class<?>[] parameterTypes) {
        if (parameterTypes == null) {
            return Void.class.getName();
        }
        StringBuilder builder = new StringBuilder();
        for (Class<?> parameterType : parameterTypes) {
            builder.append(parameterType.getName()).append(",");
        }

        return StringUtils.removeEnd(builder.toString(), ",");
    }

    public static String getMethodDesc(Method method) {
        String methodName = method.getName();
        return getMethodDesc(methodName, getParameterDesc(method.getParameterTypes()));
    }

    public static String getMethodDesc(String methodName, String parameterDesc) {
        if (StringUtils.isBlank(parameterDesc)) {
            parameterDesc = "java.lang.Void";
        }
        return methodName + "[" + parameterDesc + "]";
    }
}
