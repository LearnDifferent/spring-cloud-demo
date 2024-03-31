package com.example.springcloud.alibaba.controller;

import com.example.springcloud.alibaba.entity.Goods;
import com.example.springcloud.alibaba.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sentinel Demo
 * 两个不同的 Controller 调用同一个被 @SentinelResource 注解修饰的 service 方法。
 * 目的有两个，一是测试 spring.cloud.sentinel.web-context-unify=false 时候，是否会将监控细分到 service 层
 * 二是测试 sentinel 【流控模式】的【关联】，也就是关联模式
 *
 * @author zhou
 * @date 2024/3/31
 */
@RestController
@RequestMapping("/sentinel-demo")
@RequiredArgsConstructor
public class SentinelDemoController {

    private final GoodsService goodsService;

    @GetMapping("/from-purchased-order")
    public Goods getDetailFromPurchasedOrder(@RequestParam("id") int id) {
        Goods goods = goodsService.getDetail(id);
        goods.setDescription("Get goods detail from purchased order");
        return goods;
    }

    @GetMapping("/from-search")
    public Goods getDetailFromSearch(@RequestParam("id") int id) {
        Goods goods = goodsService.getDetail(id);
        goods.setDescription("Get goods detail from search");
        return goods;
    }
}