package com.opensoft.motanx.core;

import com.google.common.collect.Maps;
import com.opensoft.motanx.exception.ErrorCode;
import com.opensoft.motanx.exception.MotanxIllegalArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * 节点或者虚拟节点唯一标识
 * Created by kangwei on 2016/6/26.
 */
public class URL {
    private String protocol;

    private String host;

    private int port;

    //interfaceName
    private String path;

    //上下文根，服务提供者可自行指定，用于路由
    private String context;

    private Map<String, String> parameters;

    public URL(String protocol, String host, int port, String path) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        parameters = Maps.newHashMap();
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }

    /**
     * 根据给定的url串生成URL对象
     *
     * @param url url
     * @return URL对象
     */
    public static URL valueOf(String url) {
        if (StringUtils.isBlank(url)) {
            throw new MotanxIllegalArgumentException(ErrorCode.illegalArgument.getCode(), "url is null");
        }

        if (!url.contains(MotanxConstants.PROTOCOL_SEPARATOR)) {
            throw new MotanxIllegalArgumentException(ErrorCode.illegalArgument.getCode(), "url is missing protocol");
        }

        String protocol = StringUtils.substringBefore(url, MotanxConstants.PROTOCOL_SEPARATOR);
        String hostAndPort = StringUtils.substringBetween(url, MotanxConstants.PROTOCOL_SEPARATOR, MotanxConstants.PATH_SEPARATOR);

        if (!hostAndPort.contains(":")) {
            throw new MotanxIllegalArgumentException(ErrorCode.illegalArgument.getCode(), "url is missing host or port");
        }
        String host = StringUtils.substringBefore(hostAndPort, ":");
        int port = Integer.parseInt(StringUtils.substringAfter(hostAndPort, ":"));
        String pathAndParams = StringUtils.substringAfterLast(url, MotanxConstants.PATH_SEPARATOR);

        if (StringUtils.isBlank(pathAndParams)) {
            throw new MotanxIllegalArgumentException(ErrorCode.illegalArgument.getCode(), "url is missing path");
        }
        String path = pathAndParams;
        if (pathAndParams.indexOf("?") > 0) {
            path = StringUtils.substringBefore(pathAndParams, "?");
            String params = StringUtils.substringAfter(pathAndParams, "?");

            Map<String, String> paramsMap = buildParams(params);
            return new URL(protocol, host, port, path, paramsMap);
        }

        return new URL(protocol, host, port, path);
    }

    private static Map<String, String> buildParams(String params) {
        if (StringUtils.isBlank(params)) {
            return Collections.emptyMap();
        }
        Map<String, String> paramsMap = Maps.newHashMap();
        String[] kv = params.split("&");
        for (String s : kv) {
            String key = StringUtils.substringBefore(s, "=");
            String value = StringUtils.substringAfter(s, "=");
            paramsMap.put(key, value);
        }
        return paramsMap;
    }

    /**
     * 获得URL的简要uri
     *
     * @return uri
     */
    public String getUri() {
        return protocol + MotanxConstants.PROTOCOL_SEPARATOR + host + ":" + port + MotanxConstants.PATH_SEPARATOR + path;
    }

    public String getAddress() {
        return host + ":" + port;
    }

    public URL copy() {
        return new URL(protocol, host, port, path, parameters);
    }

    /**
     * 获得URL的唯一标识
     *
     * @return 唯一标识
     */
    public String getIdentifyUrl() {
        return getUri() + paramsToString();
    }

    private String paramsToString() {
        if (parameters != null && !parameters.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("?");
            for (String key : parameters.keySet()) {
                builder.append(key).append("=").append(parameters.get(key)).append("&");
            }

            return StringUtils.removeEnd(builder.toString(), "&");
        }
        return "";
    }

    public String getParameter(String key) {
        return getParameter(key, null);
    }

    public String getParameter(String key, String defaultValue) {
        return parameters.get(key) == null ? defaultValue : parameters.get(key);
    }

    public int getIntParameter(String key) {
        return getIntParameter(key, -1);
    }

    public int getIntParameter(String key, int defaultValue) {
        if (parameters.containsKey(key)) {
            String value = parameters.get(key);
            return Integer.parseInt(value);
        } else {
            return defaultValue;
        }
    }

    public long getLongParameter(String key) {
        return getLongParameter(key, -1L);
    }

    public long getLongParameter(String key, long defaultValue) {
        if (parameters.containsKey(key)) {
            String value = parameters.get(key);
            return Long.parseLong(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取版本信息，版本带在url的parameters中
     *
     * @return 版本
     */
    public String getVersion() {
        return getParameter(UrlConstants.version.getName(), UrlConstants.version.getString());
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public URL setParameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "URL{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URL url = (URL) o;

        if (port != url.port) return false;
        if (!protocol.equals(url.protocol)) return false;
        if (!host.equals(url.host)) return false;
        if (!path.equals(url.path)) return false;
        return parameters.equals(url.parameters);

    }

    @Override
    public int hashCode() {
        int result = protocol.hashCode();
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        result = 31 * result + path.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }
}
