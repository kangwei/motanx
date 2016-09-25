package com.opensoft.motanx.demo;

import javax.ws.rs.QueryParam;
import java.io.Serializable;

/**
 * Created by kangwei on 2016/8/27.
 */
public class DemoPojo implements Serializable {
    @QueryParam("name")
    private String name;

    @QueryParam("age")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DemoPojo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
