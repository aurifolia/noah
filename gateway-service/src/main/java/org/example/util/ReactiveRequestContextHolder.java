package org.example.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
public class ReactiveRequestContextHolder {
    public static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    /**
     * 获取请求信息
     *
     * @return ServerHttpRequest
     */
    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.deferContextual(contextView -> Mono.just(contextView.get(CONTEXT_KEY).getRequest()));
    }

    /**
     * 获取响应信息
     * @return ServerHttpResponse
     */
    public static Mono<ServerHttpResponse> getResponse() {
        return Mono.deferContextual(contextView -> Mono.just(contextView.get(CONTEXT_KEY).getResponse()));
    }
}
