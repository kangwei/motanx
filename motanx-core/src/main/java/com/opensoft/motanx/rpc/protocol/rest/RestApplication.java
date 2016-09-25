package com.opensoft.motanx.rpc.protocol.rest;

import com.google.common.collect.Sets;
import com.opensoft.motanx.demo.DemoService;
import com.opensoft.motanx.demo.impl.DemoServiceImpl;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.Application;
import java.util.List;
import java.util.Set;

/**
 * Created by kangwei on 2016/9/20.
 */
public class RestApplication extends Application {
    Set<Class<?>> classes = Sets.newHashSet();
    Set<Object> singletons = Sets.newHashSet();

    public RestApplication() {
        super();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    public void addResourceClasses(Class<?> cls) {
        classes.add(cls);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    public void addResourceProvider(Object provider) {
        singletons.add(provider);
    }

    public void addResourceProviders(List<Object> providers) {
        singletons.addAll(providers);
    }
}
