package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 这个是添加了 Hystrix 的 Provider
 * 如果使用了 Hystrix，主启动类要加上 @EnableCircuitBreaker
 * 如果使用了 Hystrix Dashboard，记得在 config包 下添加路径配置
 */
@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication
public class DeptProviderHystrix8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProviderHystrix8001.class, args);
    }
}
