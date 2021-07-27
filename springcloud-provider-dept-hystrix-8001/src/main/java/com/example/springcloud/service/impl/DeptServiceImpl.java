package com.example.springcloud.service.impl;

import com.example.springcloud.api.pojo.Dept;
import com.example.springcloud.dao.DeptDao;
import com.example.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public Dept findDeptById(Long id) {
        return deptDao.findDeptById(id);
    }

    @Override
    public List<Dept> findAllDepts() {
        return deptDao.findAllDepts();
    }

    @Override
    public Boolean addDept(Dept dept) {
        return deptDao.addDept(dept);
    }
}
