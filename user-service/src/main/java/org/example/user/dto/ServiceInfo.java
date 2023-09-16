package org.example.user.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
public class ServiceInfo {
    private String serviceName;
    private Integer port;
    private Map<String, String> params;
    private Map<String, String> headers;
    private LocalDateTime time;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
