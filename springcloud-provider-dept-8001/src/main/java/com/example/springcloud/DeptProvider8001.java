package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// 因为使用了 Spring Cloud Config，所以要先启动 springcloud-config-server-3001
@EnableDiscoveryClient // 如果使用了DiscoveryClient，就要开启发现客户端
@EnableEurekaClient // 记得加上客户端的注解
@SpringBootApplication
public class DeptProvider8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProvider8001.class, args);
    }
}
