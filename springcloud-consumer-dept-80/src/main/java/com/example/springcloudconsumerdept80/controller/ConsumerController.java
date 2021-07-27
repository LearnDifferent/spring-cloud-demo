package com.example.springcloudconsumerdept80.controller;

import com.example.springcloud.api.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// 消费者没有 Service 层
// 应该使用 Open Feign 或 RestTemplate 来调用 Provider
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

//    // 如果固定从某个 Provider 获取，就把它写死：
//    private static final String PROVIDER_URL_PREFIX = "http://localhost:8001";

    // 如果 RestTemplate 使用了 Ribbon 负载均衡，就不要写死
    // 而是使用变量 application name，也就是 host 来作为识别 provider(s) 的依据
    // 这样的话，Consumer 就不需要关心地址，可以直接动态调用名称相同的 provider 了
    private static final String PROVIDER_URL_PREFIX = "http://springcloud-provider-dept";

    // Provider 那里的 Controller 使用的是 Get，使用这里也使用 Get
    @GetMapping("/consumer/dept/find/{id}")
    public Dept findDeptById(@PathVariable("id") Long id) {
        // 这里要使用 RestTemplate 的 get 开头的方法
        // 连接到 provider 的 Controller 里面的 /dept/find/{id}
        // 返回值是 Dept 实体类
        return restTemplate.getForObject(PROVIDER_URL_PREFIX
                + "/dept/find/" + id, Dept.class);
    }

    @GetMapping("/consumer/dept/find")
    public List<Dept> findAllDepts() {
        return restTemplate.getForObject(PROVIDER_URL_PREFIX
                + "/dept/find/", List.class);
    }

    // Provider 那里的是 post，所以这里也用 post
    @PostMapping("/consumer/dept/add")
    public Boolean addDept(@RequestBody Dept dept) {
        // post 要记得传入 Request（这里的 Request 可以传入实体类）
        return restTemplate.postForObject(PROVIDER_URL_PREFIX
                + "/dept/add", dept, Boolean.class);
    }
}
