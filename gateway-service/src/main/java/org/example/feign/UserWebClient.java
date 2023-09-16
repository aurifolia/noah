package org.example.feign;

import org.example.dto.ResultDTO;
import org.example.dto.ServiceInfo;
import org.example.util.ReactiveRequestContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
@Component
public class UserWebClient {
    private static final String BASE_URL = "http://user-service";
    private static final Duration DURATION = Duration.ofSeconds(10);
    private static final ParameterizedTypeReference<ResultDTO<ServiceInfo>> TYPE = new ParameterizedTypeReference<>() {};

    @Autowired
    private WebClient.Builder webClientBuilder;

    public ResultDTO<ServiceInfo> time() {
        return webClientBuilder.build()
                .get()
                .uri(BASE_URL + "/time")
                .retrieve()
                .bodyToMono(TYPE)
                .block(DURATION);
    }

    public Mono<ResultDTO<ServiceInfo>> timeWithHeader() {
        return ReactiveRequestContextHolder.getRequest().flatMap(request -> {
            HttpHeaders headers = request.getHeaders();
            List<String> params = new ArrayList<>();
            request.getQueryParams().forEach((k, v) -> params.add(String.format("%s=%s", k, v)));
            String url = BASE_URL + "/time";
            if (!params.isEmpty()) {
                url = url + "?" + String.join("&", params);
            }
            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .headers(item -> {
                        item.putAll(headers);
                    })
                    .retrieve()
                    .bodyToMono(TYPE);
        });
    }
}
