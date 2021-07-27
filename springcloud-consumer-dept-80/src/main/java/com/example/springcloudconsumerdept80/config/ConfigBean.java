package com.example.springcloudconsumerdept80.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean {

    // 可以在 RestTemplate 上配置负载均衡
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // 默认是轮询，可以通过加入 Spring 容器来改变算法策略
    @Bean
    public IRule rule() {
        // 这里改成随机
        return new RandomRule();
    }

}
