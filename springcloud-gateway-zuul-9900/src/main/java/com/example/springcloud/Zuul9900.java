package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy // 网关的注解
@SpringBootApplication
public class Zuul9900 {
    public static void main(String[] args) {
        SpringApplication.run(Zuul9900.class, args);
    }
}
