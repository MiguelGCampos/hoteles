package com.hoteles.huespedes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hoteles.commons.clients")
@ComponentScan(basePackages = {"com.hoteles.huespedes", "com.hoteles.commons"})
public class HuespedesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuespedesApplication.class, args);
    }

}
