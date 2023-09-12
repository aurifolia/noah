package org.example;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/21
 **/
@SpringBootApplication
public class PureApplication {
    public static void main(String[] args) {
        SpringApplication.run(PureApplication.class, args);
    }

    @Bean
    public FactoryBean<ThreadLocal> getThreadLocal001() {
        return new FactoryBean<>() {

            @Override
            public ThreadLocal getObject() throws Exception {
                return new ThreadLocal();
            }

            @Override
            public Class<?> getObjectType() {
                return ThreadLocal.class;
            }
        };
    }
}
