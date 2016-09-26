package com.opensoft.motanx.utils;

import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by kangwei on 2016/9/26.
 */
public final class CloseableUtils {
    private static final Logger log = LoggerFactory.getLogger(CloseableUtils.class);

    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }

        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                log.error("close resource {} error", closeable.getClass().getName(), e);
            }
        }
    }
}
