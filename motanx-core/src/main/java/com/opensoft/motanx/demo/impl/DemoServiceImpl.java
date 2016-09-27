package com.opensoft.motanx.demo.impl;

import com.google.common.collect.Maps;
import com.opensoft.motanx.demo.DemoPojo;
import com.opensoft.motanx.demo.DemoService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kangwei on 2016/8/27.
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String hello() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello rpc";
    }

    @Override
    public String hello(String id) {
        return id;
    }

    @Override
    public String sayHello(String s) {
        return s;
    }

    @Override
    public List<String> list() {
        return Arrays.asList("hello");
    }

    @Override
    public Map<String, String> map() {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("key", "value");
        return map;
    }

    @Override
    public DemoPojo pojo(DemoPojo pojo) {
        return pojo;
    }

    @Override
    public DemoPojo showPojo() {
        DemoPojo pojo = new DemoPojo();
        pojo.setAge(12);
        pojo.setName("skda");
        return pojo;
    }
}
