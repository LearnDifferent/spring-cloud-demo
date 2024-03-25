package com.example.springcloud.alibaba.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Query Provider Service
 *
 * @author zhou
 * @date 2024/3/25
 */
@FeignClient(value = "nacos-provider")
@Component
public interface ProviderService {

    /**
     * Query GET nacos-provider/provider/api
     *
     * @return result
     */
    @GetMapping("/provider/api")
    String providerApi();
}