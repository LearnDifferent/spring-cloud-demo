package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server 端，记得使用 @EnableConfigServer 注解
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServer3001 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServer3001.class, args);
    }
}
