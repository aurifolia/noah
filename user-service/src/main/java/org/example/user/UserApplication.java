package org.example.user;

import jakarta.servlet.http.HttpServletRequest;
import org.example.user.dto.ResultDTO;
import org.example.user.dto.ServiceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户应用
 *
 * @author VNElinpe
 * @since 2023/3/19
 **/
@RestController
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    private final AtomicInteger integer = new AtomicInteger();

    @Value("${server.port}")
    private String port;

    @GetMapping("/time")
    public ResultDTO<ServiceInfo> time(HttpServletRequest request) {
        if (integer.getAndIncrement() % 7 != 0) {
            int a = 1 / 0;
        }
        Map<String, String> result = new HashMap<>();
        result.put("service", "user-service");
        result.put("port", port);
        result.put("integer", String.valueOf(integer.get()));
        result.put("dateTime", LocalDateTime.now().toString());
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceName("user-service");
        serviceInfo.setPort(integer.get());
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            headers.put(s, request.getHeader(s));
        }
        serviceInfo.setParams(params);
        serviceInfo.setHeaders(headers);
        serviceInfo.setTime(LocalDateTime.now());
        return ResultDTO.success(serviceInfo);
    }
}
