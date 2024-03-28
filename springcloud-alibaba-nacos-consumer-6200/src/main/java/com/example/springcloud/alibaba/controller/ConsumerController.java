package com.example.springcloud.alibaba.controller;

import com.example.springcloud.ProviderClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ConsumerController {

    private final ProviderClient providerClient;

    private static final UUID ID = UUID.randomUUID();

    @GetMapping("/api")
    public String hello() {
        // result from provider
        String result = providerClient.providerApi();
        return "[Consumer ID: " + ID + "]: " + result;
    }
}
