package com.example.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Nacos Consumer Demo
 *
 * @author zhou
 * @date 2024/3/25
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class NacosConsumer6200 {

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumer6200.class, args);
    }
}
