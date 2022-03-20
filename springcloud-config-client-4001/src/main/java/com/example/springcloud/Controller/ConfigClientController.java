package com.example.springcloud.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 读取一下当前服务的配置
 * 可以看看 application.yml 和 bootstrap.yml 一起使用后以哪个为主
 */
@RestController
public class ConfigClientController {

    //通过配置名称，获取配置
    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String port;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServerUrl;

    // 注意，因为读取了远程的配置，所以访问的时候，使用的是远程配置中的端口号
    // （这里的远程配置的端口号为 8001）
    @RequestMapping
    public String getConfig() {
        return applicationName + "\t"
                + port + "\t"
                + instanceId + "\t"
                + eurekaServerUrl;
    }
}
