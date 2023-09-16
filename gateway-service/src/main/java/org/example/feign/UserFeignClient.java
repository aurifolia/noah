package org.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
@FeignClient("user-service")
public interface UserFeignClient {
    @GetMapping("/time")
    Data time();

    class Data {
        private String dateTime;
        private String service;
        private String integer;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getInteger() {
            return integer;
        }

        public void setInteger(String integer) {
            this.integer = integer;
        }
    }
}
