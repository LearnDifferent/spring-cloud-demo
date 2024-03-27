package com.example.springcloud.alibaba.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RequestMapping("/config")
@RefreshScope
public class ConfigDemoController {

    /**
     * 需要去 Nacos 控制台创建 nacos-config-demo-dev.yaml 配置，并增加 config-test.str
     */
    @Value("${config-test.str}")
    private String str;

    @GetMapping
    public String getConfig() {
        return str;
    }
}
