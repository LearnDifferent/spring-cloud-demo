package com.example.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Nacos Provider Demo
 *
 * @author zhou
 * @date 2024/3/25
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProvider6100 {

    public static void main(String[] args) {
        SpringApplication.run(NacosProvider6100.class, args);
    }
}
