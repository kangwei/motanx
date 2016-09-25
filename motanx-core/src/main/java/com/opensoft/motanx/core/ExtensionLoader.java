package com.opensoft.motanx.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensoft.motanx.exception.MotanxFrameworkException;
import com.opensoft.motanx.logger.Logger;
import com.opensoft.motanx.logger.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 扩展SPI加载器，用于装载不同的SPI
 * 读取所有的SPI，根据SPI的名字加载不同的实例
 * Created by kangwei on 2016/8/26.
 */
public class ExtensionLoader<T> {
    public static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

    public static final String PREFIX = "spis/";

    private static ConcurrentMap<Class<?>, ExtensionLoader> extensionLoaderMap = Maps.newConcurrentMap();

    private ConcurrentMap<String, T> singletonInstances = Maps.newConcurrentMap();

    private ConcurrentMap<String, Class<T>> extensionClasses = Maps.newConcurrentMap();

    private Class<T> type;

    private ClassLoader classLoader;

    private volatile boolean init = false;

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        checkType(type);

        if (!extensionLoaderMap.containsKey(type)) {
            loadExtensionLoaderMap(type);
        }

        return extensionLoaderMap.get(type);
    }

    private static synchronized <T> void loadExtensionLoaderMap(Class<T> type) {
        if (!extensionLoaderMap.containsKey(type)) {
            extensionLoaderMap.putIfAbsent(type, new ExtensionLoader<T>(type));
        }
    }

    private static <T> void checkType(Class<T> type) {
        if (type == null) {
            throw new MotanxFrameworkException("spi type must be not null");
        }
        if (!type.isInterface()) {
            throw new MotanxFrameworkException("spi type " + type + " must be interface");
        }

        if (!type.isAnnotationPresent(Spi.class)) {
            throw new MotanxFrameworkException("spi type " + type + " must be Spi");
        }
    }

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    private ExtensionLoader(Class<T> type, ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
    }

    public Class<T> getExtensionClass(String name) {
        initIfNeed();
        return extensionClasses.get(name);
    }

    public T getExtension(String name) {
        initIfNeed();
        Class<T> extensionClass = getExtensionClass(name);
        if (extensionClass == null) {
            throw new MotanxFrameworkException("extensionClass " + name + " is not found");
        }
        Spi spi = extensionClass.getAnnotation(Spi.class);
        Scope scope = spi.scope();
        try {
            if (Scope.SINGLETON.equals(scope)) {
                return getSingletonInstance(name);
            } else {
                return extensionClass.newInstance();
            }
        } catch (Exception e) {
            throw new MotanxFrameworkException(name + " create instance error", e);
        }
    }

    private T getSingletonInstance(String name) throws IllegalAccessException, InstantiationException {
        if (!singletonInstances.containsKey(name)) {
            synchronized (singletonInstances) {
                if (!singletonInstances.containsKey(name)) {
                    Class<T> extensionClass = getExtensionClass(name);
                    T t = extensionClass.newInstance();
                    singletonInstances.putIfAbsent(name, t);
                }
            }
        }

        return singletonInstances.get(name);
    }

    private void initIfNeed() {
        if (!init) {
            loadExtensionLoader();
            init = true;
        }
    }

    private synchronized void loadExtensionLoader() {
        //再次检查，防止并发加载
        if (init) {
            return;
        }

        String fullPath = PREFIX + type.getName();
        Enumeration<URL> urls;
        try {
            if (classLoader == null) {
                urls = ClassLoader.getSystemResources(fullPath);
            } else {
                urls = classLoader.getResources(fullPath);
            }
        } catch (IOException e) {
            logger.error("loadExtensionClass named {} error", type.getName());
            throw new MotanxFrameworkException("loadExtensionClass named " + type.getName() + " error");
        }

        if (urls != null) {
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                parseUrl(url);
            }
        }
    }

    private void parseUrl(URL url) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = url.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> classNames = Lists.newArrayList();
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!classNames.contains(line)) {
                    classNames.add(line);
                }
            }
            for (String className : classNames) {
                Class<T> aClass;
                if (classLoader == null) {
                    aClass = (Class<T>) Class.forName(className);
                } else {
                    aClass = (Class<T>) Class.forName(className, true, classLoader);
                }

                checkExtensionClass(aClass);
                Spi spi = aClass.getAnnotation(Spi.class);
                String spiName = spi.name();
                extensionClasses.putIfAbsent(spiName, aClass);

            }
        } catch (IOException e) {
            logger.error("read file " + url.toString() + " error", e);
            throw new MotanxFrameworkException("read file " + url.toString() + " error");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new MotanxFrameworkException(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("close inputStream error", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("close reader error", e);
                }
            }
        }
    }

    private void checkExtensionClass(Class<?> aClass) {
        if (!Modifier.isPublic(aClass.getModifiers())) {
            throw new MotanxFrameworkException(aClass.getName() + " is not public");
        }

        checkConstructors(aClass);
        if (!aClass.isAnnotationPresent(Spi.class)) {
            throw new MotanxFrameworkException(aClass.getName() + " is not spi");
        }
    }

    private void checkConstructors(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();
        if (constructors == null) {
            throw new MotanxFrameworkException(aClass.getName() + " has no constructor");
        }

        boolean hasOnePublic = false;
        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                hasOnePublic = true;
                break;
            }
        }

        if (!hasOnePublic) {
            throw new MotanxFrameworkException(aClass.getName() + " has no public constructor");
        }
    }
}