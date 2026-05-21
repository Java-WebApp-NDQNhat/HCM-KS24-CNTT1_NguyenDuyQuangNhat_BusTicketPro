package com.re.trans_route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   // bat. tinh' nang len lich. co' san~ cua? spring boot
public class TransRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransRouteApplication.class, args);
    }

}
