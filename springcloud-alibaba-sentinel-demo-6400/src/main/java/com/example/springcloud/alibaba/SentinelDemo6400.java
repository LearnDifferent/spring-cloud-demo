package com.example.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Sentinel Demo
 * 两个不同的 Controller 调用同一个被 @SentinelResource 注解修饰的 service 方法。
 * 目的有两个，一是测试 spring.cloud.sentinel.web-context-unify=false 时候，是否会将监控细分到 service 层
 * 二是测试 sentinel 【流控模式】的【关联】，也就是关联模式
 *
 * @author zhou
 * @date 2024/3/31
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SentinelDemo6400 {

    public static void main(String[] args) {
        SpringApplication.run(SentinelDemo6400.class, args);
    }
}
