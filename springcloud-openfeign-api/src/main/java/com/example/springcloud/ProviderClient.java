package com.example.springcloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Query Provider Service
 *
 * @author zhou
 * @date 2024/3/25
 * @see <a href="https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel#feign-%E6%94%AF%E6%8C%81">Sentinel 官方文档</a>
 * @see ProviderClientConfig
 * @see ProviderClientFallbackFactory
 */
@FeignClient(value = "nacos-provider",
             configuration = ProviderClientConfig.class,
             fallbackFactory = ProviderClientFallbackFactory.class)
public interface ProviderClient {

    /**
     * Query GET nacos-provider/provider/api
     *
     * @return result
     */
    @GetMapping("/provider/api")
    String providerApi();
}