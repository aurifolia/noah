package org.example;

import feign.codec.Decoder;
import org.example.dto.ResultDTO;
import org.example.dto.ServiceInfo;
import org.example.feign.UserFeignClient;
import org.example.feign.UserWebClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/12
 **/
@RestController
@EnableFeignClients
@SpringBootApplication
public class GatewayService implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(GatewayService.class, args);
    }

    @GetMapping("/myFallback")
    public Mono<String> fallback() {
        return Mono.fromSupplier(() -> LocalDateTime.now().toString());
    }

    @Autowired
    private UserWebClient userWebClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ResultDTO<ServiceInfo> time = userWebClient.time();
        System.out.println();
    }
}
