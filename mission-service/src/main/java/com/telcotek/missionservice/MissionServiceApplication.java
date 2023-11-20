package com.telcotek.missionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;


@SpringBootApplication
@EnableWebSocket
@EnableDiscoveryClient
@EnableScheduling
public class MissionServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(MissionServiceApplication.class, args);
    }

}
