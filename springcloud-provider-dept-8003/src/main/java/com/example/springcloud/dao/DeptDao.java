package com.example.springcloud.dao;

import com.example.springcloud.api.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeptDao {

    Dept findDeptById(Long id);

    List<Dept> findAllDepts();

    Boolean addDept(Dept dept);
}
