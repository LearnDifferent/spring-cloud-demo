package com.example.springcloudconsumerdept80.service;

import com.example.springcloud.api.pojo.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Feign Hystrix Fallbacks：
 * Dept 的消费者的 Feign Client 如果发现服务提供者出错
 * ，就跳转到当前这个备选方案类上。
 * 使用的时候，记得在消费者端的配置文件添加 Hystrix 的配置
 * <p>
 * 需要实现 FallbackFactory<原方案接口> 的接口
 * 返回的时候，要 return new 原方案接口
 * ，并且用匿名类实现接口，完成备选方案
 * <p>
 * 记得一定要加上 @Component 注解
 */
@Component
public class DeptClientFallback implements FallbackFactory<DeptClient> {

    @Override
    public DeptClient create(Throwable throwable) {
        return new DeptClient() {

            @Override
            public Dept findDeptById(Long id) {
                return new Dept()
                        .setDeptno(id)
                        .setDname("查询id的业务暂时关闭")
                        .setDb_source("错误信息为" + throwable.getMessage());
            }

            @Override
            public List<Dept> findAllDepts() {
                return null;
            }

            @Override
            public Boolean addDept(Dept dept) {
                return null;
            }
        };
    }
}
