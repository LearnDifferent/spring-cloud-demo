package com.example.springcloud.alibaba.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provider
 *
 * @author zhou
 * @date 2024/3/25
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

    private static final UUID ID = UUID.randomUUID();

    @Value("${server.port}")
    private Integer port;

    @Value("${spring.cloud.nacos.discovery.cluster-name}")
    private String clusterName;

    @GetMapping("/api")
    public String hello() {
        return "Hello. [From Provider " + ID  + " & Port: " + port
                + " & ClusterName: " + clusterName + "]";
    }
}
