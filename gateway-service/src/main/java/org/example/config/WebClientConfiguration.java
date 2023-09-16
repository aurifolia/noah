package org.example.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
@Configuration
public class WebClientConfiguration {
    /**
     * WebClient从注册中心获取服务地址
     * @return
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
