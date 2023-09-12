package org.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @GetMapping("/time/{id}")
    public Map<String, String> time(@PathVariable Integer id) {
        if (id % 2 == 0) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Map<String, String> result = new HashMap<>();
        result.put("service", "user-service");
        result.put("dateTime", LocalDateTime.now().toString());
        return result;
    }
}
