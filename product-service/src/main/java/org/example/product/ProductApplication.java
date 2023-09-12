package org.example.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/4/8
 **/
@RestController
@SpringBootApplication
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @GetMapping("/time")
    public Map<String, String> time() {
        Map<String, String> result = new HashMap<>();
        result.put("service", "product-service");
        result.put("dateTime", LocalDateTime.now().toString());
        return result;
    }
}
