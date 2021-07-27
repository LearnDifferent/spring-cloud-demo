package com.example.springcloud.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixConfig {

    // 在 Provider 端添加可以被 Hystrix Dashboard 监控的 Metrics Stream 流
    // 这样就可以通过访问这个网址来监控该 Provider 了： $host:$port/hystrix.stream
    @Bean
    public ServletRegistrationBean hystrixMetricsStreamServlet() {

        // 创建 Metrics Stream 流的 Servlet
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();

        // 用该 Steam 的 Servlet 创建 servlet 注册 bean
        ServletRegistrationBean registration = new ServletRegistrationBean(streamServlet);

        // 给这个 servlet 创建 url
        registration.addUrlMappings("/hystrix.stream");

        // 返回
        return registration;
    }
}
