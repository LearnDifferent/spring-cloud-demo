package com.example.springcloud.alibaba.controller;

import com.example.springcloud.alibaba.config.ConfigDemoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nacos Configuration Demo 不需要 {@link org.springframework.cloud.context.config.annotation.RefreshScope} 注解实现配置自动刷新
 * 这种方法即使 Nacos 没有相关配置，也不会报错 NPE
 *
 * @author zhou
 * @date 2024/3/28
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigDemoController {

    private final ConfigDemoProperties configDemoProperties;

    @GetMapping
    public String getConfig() {
        return configDemoProperties.getStr();
    }
}
