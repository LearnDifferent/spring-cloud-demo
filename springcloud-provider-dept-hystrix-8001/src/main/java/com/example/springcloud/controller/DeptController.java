package com.example.springcloud.controller;

import com.example.springcloud.api.pojo.Dept;
import com.example.springcloud.service.DeptService;
import com.example.springcloud.service.impl.DeptServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

    // 根据 ID 查找的首选方案
    // 备选方案使用 @HystrixCommand 引入
    @GetMapping("/dept/find/{id}")
    @HystrixCommand(fallbackMethod = "hystrixFindDeptById")
    public Dept findDeptById(@PathVariable("id") Long id) {
        Dept dept = deptService.findDeptById(id);
        if (dept == null) {
            //如果找不到，直接抛出异常，让备选方案执行
            throw new RuntimeException("找不到该数据");
        }
        return dept;
    }

    // 根据 ID 查找的备选方案
    public Dept hystrixFindDeptById(@PathVariable("id") Long id) {

        // 如果出错，返回一个自己编造的 Dept 错误信息
        return new Dept()
                .setDeptno(id)
                .setDname("id为" + id + "的数据不存在")
                .setDb_source("no such data in MySQL");
    }

    @GetMapping("/dept/find")
    public List<Dept> findAllDepts() {
        return deptService.findAllDepts();
    }

    @PostMapping("/dept/add")
    public Boolean addDept(@RequestBody Dept dept) {
        return deptService.addDept(dept);
    }

}
