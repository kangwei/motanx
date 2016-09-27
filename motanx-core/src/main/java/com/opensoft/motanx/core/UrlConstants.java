package com.opensoft.motanx.core;

/**
 * Created by kangwei on 2016/8/25.
 */
public enum UrlConstants {
    version("version", "1.0"),
    delay("delay", 30000),
    filters("filters", ""),
    async("async", false),
    server("server", "jetty"),
    nodeType("nodeType", "provider"),
    threadNum("threadNum", 200),
    connectionTimeout("connectionTimeout", 5000);

    UrlConstants(String name, String s) {
        this.name = name;
        this.string = s;
    }

    UrlConstants(String name, int anInt) {
        this.name = name;
        this.anInt = anInt;
    }

    UrlConstants(String name, long aLong) {
        this.name = name;
        this.aLong = aLong;
    }

    UrlConstants(String name, boolean aBoolean) {
        this.name = name;
        this.aBoolean = aBoolean;
    }

    private String name;
    private String string;
    private int anInt;
    private long aLong;
    private boolean aBoolean;

    public String getName() {
        return name;
    }

    public String getString() {
        return string;
    }

    public int getInt() {
        return anInt;
    }

    public long getLong() {
        return aLong;
    }

    public boolean getBoolean() {
        return aBoolean;
    }
}
