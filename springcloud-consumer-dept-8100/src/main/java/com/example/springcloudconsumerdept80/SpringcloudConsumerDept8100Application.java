package com.example.springcloudconsumerdept80;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 消费者使用了 Ribbon 后需要 Eureka
 *
 * @author zhou
 * @date 2024/3/28
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringcloudConsumerDept8100Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudConsumerDept8100Application.class, args);
    }

}
