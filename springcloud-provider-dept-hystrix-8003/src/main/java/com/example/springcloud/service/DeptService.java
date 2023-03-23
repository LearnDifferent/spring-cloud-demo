package com.example.springcloud.service;

import com.example.springcloud.api.pojo.Dept;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DeptService {
    Dept findDeptById(Long id);

    List<Dept> findAllDepts();

    Boolean addDept(Dept dept);
}
