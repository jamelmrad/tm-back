package com.telcotek.uaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.telcotek.uaservice.model")
@EnableJpaRepositories(basePackages = "com.telcotek.uaservice.repository")
public class UaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaServiceApplication.class, args);
    }

}
