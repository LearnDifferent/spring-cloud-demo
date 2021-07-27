package com.example.springcloud.controller;

import com.example.springcloud.api.pojo.Dept;
import com.example.springcloud.service.DeptService;
import com.example.springcloud.service.impl.DeptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {

    @Autowired
    private DeptServiceImpl deptService;

    // 导入接口：org.springframework.cloud.client.discovery.DiscoveryClient
    // 该接口的实现类 EurekaDiscoveryClient 可以获取一些配置信息，得到具体的微服务
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/dept/find/{id}")
    public Dept findDeptById(@PathVariable("id") Long id) {
        return deptService.findDeptById(id);
    }

    @GetMapping("/dept/find")
    public List<Dept> findAllDepts() {
        return deptService.findAllDepts();
    }

    @PostMapping("/dept/add")
    public Boolean addDept(@RequestBody Dept dept) {
        return deptService.addDept(dept);
    }

    // 通过注册的微服务，获取一些信息
    @GetMapping("/dept/discovery")
    public Object discovery() {
        // 获取已经注册的微服务的列表
        List<String> services = discoveryClient.getServices();
        // 打印微服务列表，一般可以通过遍历来获取具体的微服务名称（service id/application name）
        System.out.println("services:" + services);

        // 通过 application name（也就是service id），获取具体的微服务实例，一般要通过遍历获得，这里直接指定
        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-provider-dept");
        instances.forEach((i) -> System.out.println(i.getInstanceId() + "\t"
                + i.getServiceId() + "\t" + i.getHost() + "\t" + i.getPort()
                + "\t" + i.getScheme() + "\t" + i.getMetadata() + "\t" + i.getUri()));
        return this.discoveryClient;
    }
}
