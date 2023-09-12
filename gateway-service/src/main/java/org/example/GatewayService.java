package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/12
 **/
@RestController
@SpringBootApplication
public class GatewayService {
    public static void main(String[] args) {
        SpringApplication.run(GatewayService.class, args);
    }

    @GetMapping("/myFallback")
    public Mono<String> fallback() {
        return Mono.fromSupplier(() -> LocalDateTime.now().toString());
    }
}
