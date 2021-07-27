package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 这里是使用 Hystrix Dashboard 的 Consumer，使用 @EnableHystrixDashboard
 * 注解会扫描其它微服务中带 @HystrixCommand 注解的方法
 * ，然后 implement a proxy for it and monitor its calls
 */
@EnableHystrixDashboard
@SpringBootApplication
public class ConsumerHystrixDashboard9001Application {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerHystrixDashboard9001Application.class, args);
    }

}
