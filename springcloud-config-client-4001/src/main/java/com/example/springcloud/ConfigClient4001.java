package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 先启动另外一个 Config Server 再启动当前的 Config Client
 */
@SpringBootApplication
public class ConfigClient4001 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClient4001.class, args);
    }
}
