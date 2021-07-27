package com.example.springcloudconsumerdept80.service;

import com.example.springcloud.api.pojo.Dept;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 如果不需要获取错误信息（Throwable cause）
 * ，就直接重写接口，实现 fall back 后的业务逻辑
 * ，再在原接口上添加 fallback（不是fallbackFactory）属性来导入当前类
 */
@Component
class DeptClientFallback_Simple implements DeptClient {

    @Override
    public Dept findDeptById(Long id) {
        return new Dept()
                .setDeptno(id)
                .setDname("查询id为" + id + "的业务暂时关闭")
                .setDb_source("不存在于任何数据库中");
    }

    @Override
    public List<Dept> findAllDepts() {
        // 省略 fall back 逻辑
        return null;
    }

    @Override
    public Boolean addDept(Dept dept) {
        // 省略 fall back 逻辑
        return null;
    }
}