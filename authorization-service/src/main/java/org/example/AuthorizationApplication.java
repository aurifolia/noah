package org.example;

import org.owasp.validator.html.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URL;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/6/8
 **/
@SpringBootApplication
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }

    public static class Test {
        public static void main(String[] args) throws IOException, PolicyException, ScanException {
            URL resource = Test.class.getClassLoader().getResource("antisamy-ebay.xml");
            Policy policy = Policy.getInstance(resource.openStream());
            AntiSamy antiSamy = new AntiSamy();
            CleanResults results = antiSamy.scan("q23x23r<div>34242342342</div>", policy);
            System.out.println();
        }
    }
}
