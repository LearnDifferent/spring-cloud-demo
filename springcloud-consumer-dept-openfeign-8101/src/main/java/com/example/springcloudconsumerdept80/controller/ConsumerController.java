package com.example.springcloudconsumerdept80.controller;

import com.example.springcloud.api.pojo.Dept;
import com.example.springcloudconsumerdept80.service.DeptClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试 Open Feign Client 完成负载均衡的调用
 */
@RestController
public class ConsumerController {

    @Autowired
    private DeptClient deptClient;

    @GetMapping("/consumer/dept/find/{id}")
    public Dept findDeptById(@PathVariable("id") Long id) {
        return deptClient.findDeptById(id);
    }

    @GetMapping("/consumer/dept/find")
    public List<Dept> findAllDepts() {
        return deptClient.findAllDepts();
    }

    @PostMapping("/consumer/dept/add")
    public Boolean addDept(@RequestBody Dept dept) {
        return deptClient.addDept(dept);
    }
}