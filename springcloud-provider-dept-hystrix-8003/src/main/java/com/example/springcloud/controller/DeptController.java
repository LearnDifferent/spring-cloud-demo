package com.example.springcloud.controller;

import com.example.springcloud.api.pojo.Dept;
import com.example.springcloud.service.DeptService;
import com.example.springcloud.service.impl.DeptServiceImpl;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 熔断触发降级
     *
     * @param id id
     * @return 例子
     */
    @GetMapping("/dept/find/{id}")
    // Hystrix 配置
    @HystrixCommand(
            // 配置熔断策略
            commandProperties = {
                    // 具体的配置要到 HystrixCommandProperties 类中的 HystrixCommandProperties 方法中查找：
                    // 比如，找到 circuitBreaker.enabled 属性，设定为 true，表示开启熔断
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // circuitBreaker.requestVolumeThreshold 请求次数超过__次
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    // circuitBreaker.sleepWindowInMilliseconds 熔断的时间超过__毫秒
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    // circuitBreaker.errorThresholdPercentage 异常率超过__%
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
            },
            fallbackMethod = "meltdownFallback"
    )
    public Dept findDeptById(@PathVariable("id") Long id) {

        if (id.equals(0L)) {
            // 如果 ID 是 0，表示这个是正常请求
            System.out.println("正常请求，会打印这段话。说明这个正常方法被执行了");
        }

        Dept dept = deptService.findDeptById(id);
        if (dept == null) {
            throw new RuntimeException("找不到该数据");
        }
        return dept;
    }

    public Dept meltdownFallback(@PathVariable("id") Long id) {

        return new Dept()
                .setDeptno(id)
                .setDname("id为" + id + "的数据不存在")
                .setDb_source("no such data in MySQL");
    }

    /**
     * 请求超时触发降级
     *
     * @return 例子
     */
    @GetMapping("/dept/find")
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            },
            fallbackMethod = "timeoutFallback"
    )
    public List<Dept> findAllDepts() {
        return deptService.findAllDepts();
    }

    public List<Dept> timeoutFallback() {
        return Lists.newArrayList();
    }

    @PostMapping("/dept/add")
    public Boolean addDept(@RequestBody Dept dept) {
        return deptService.addDept(dept);
    }

}
