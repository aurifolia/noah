package org.example.controller;

import jakarta.annotation.PostConstruct;
import org.example.dto.ResultDTO;
import org.example.dto.ServiceInfo;
import org.example.feign.UserWebClient;
import org.example.util.ReactiveRequestContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
@RestController
@RequestMapping("/v1")
public class TestController {
    public ThreadPoolExecutor pool;

    @PostConstruct
    private void init() {
        pool = new ThreadPoolExecutor(8, 8, 1, TimeUnit.MINUTES,
                new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @GetMapping("/test")
    public Mono<Map<String, String>> test() {
        Mono<ServerHttpRequest> request = ReactiveRequestContextHolder.getRequest();
        Map<String, String> map = new HashMap<>();
        map.put("service", "gateway");
        map.put("dateTime", LocalDateTime.now().toString());
        return request.map(req -> {
            map.put("uri", req.getURI().getPath());
            return map;
        });
    }

    @GetMapping("/test1")
    public Mono<Map<String, String>> test1() {
        try {
            return pool.submit(() -> {
                Mono<ServerHttpRequest> request = ReactiveRequestContextHolder.getRequest();
                Map<String, String> map = new HashMap<>();
                map.put("service", "gateway");
                map.put("thread", Thread.currentThread().getName());
                map.put("dateTime", LocalDateTime.now().toString());
                return request.map(req -> {
                    map.put("uri", req.getURI().getPath());
                    map.put("params", req.getQueryParams().toString());
                    return map;
                });
            }).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private UserWebClient userWebClient;

    @GetMapping("/func001")
    public Mono<ResultDTO<ServiceInfo>> func001() {
        return userWebClient.timeWithHeader();
    }
}
