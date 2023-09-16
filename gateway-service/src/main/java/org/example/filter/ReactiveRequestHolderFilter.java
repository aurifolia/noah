package org.example.filter;

import org.example.util.ReactiveRequestContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
@Component
public class ReactiveRequestHolderFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .contextWrite(context -> context.put(ReactiveRequestContextHolder.CONTEXT_KEY, exchange));
    }
}
