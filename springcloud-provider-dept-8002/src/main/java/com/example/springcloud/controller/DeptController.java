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

    @GetMapping("/dept/discovery")
    public Object discovery() {

        List<String> services = discoveryClient.getServices();
        System.out.println("services:" + services);

        List<ServiceInstance> instances = discoveryClient.getInstances("springcloud-provider-dept");
        instances.forEach((i) -> System.out.println(i.getInstanceId() + "\t"
                + i.getServiceId() + "\t" + i.getHost() + "\t" + i.getPort()
                + "\t" + i.getScheme() + "\t" + i.getMetadata() + "\t" + i.getUri()));
        return this.discoveryClient;
    }
}
