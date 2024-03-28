package com.example.springcloud.alibaba.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 不需要 {@link org.springframework.cloud.context.config.annotation.RefreshScope} 注解实现配置自动刷新。
 * 这种方法还有一个好处，就是即使读取不到 Nacos 的配置，也不会报错 NPE
 *
 * @author zhou
 * @date 2024/3/28
 */
@ConfigurationProperties(prefix = "config-test")
@Component
@Data
public class ConfigDemoProperties {

    /**
     * nacos-config-demo-dev.yaml 配置中的 config-test.str
     */
    private String str;

}
