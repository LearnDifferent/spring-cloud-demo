package com.example.springcloud.alibaba.controller;

import com.example.springcloud.alibaba.service.ProviderService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Consumer
 *
 * @author zhou
 * @date 2024/3/25
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    private final ProviderService providerService;

    private static final UUID ID = UUID.randomUUID();

    public ConsumerController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/api")
    public String hello() {
        // result from provider
        String result = providerService.providerApi();
        return "[Consumer ID: " + ID + "]: " + result;
    }
}
