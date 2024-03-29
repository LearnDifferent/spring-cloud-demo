package com.example.springcloudgateway9800;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Cloud Gateway 示例
 * 注意这里设置了 Global Filters，所以请求需要添加参数 ?auth=admin
 *
 * @author zhou
 * @date 2024/3/29
 * @see com.example.springcloudgateway9800.config.SimpleAuthGlobalFilter
 */
@SpringBootApplication
public class SpringcloudGateway9800Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudGateway9800Application.class, args);
    }

}
