package com.example.springcloudconsumerdept80;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 这里使用 Open Feign 示范消费者的写法
 * 这里的 Hystrix 功能是 Feign 内置的，所以不需要导入 Hystrix 依赖 ，也不需要使用 Hystrix 相关的注解
 * ，但是配置文件一定要让 feign.hystrix.enabled = true
 * <p>
 * 记得在主启动类加上 @EnableFeignClients 注解 ，
 * 如果 {@link com.example.springcloudconsumerdept80.service.DeptClient Feign Client 相关的类} 没有使用 @Component 加入到容器
 * ，就要使用 @EnableFeignClients 的 basePackageClasses 或 basePackages 属性来扫描 Feign 客户端（接口）
 */
@EnableFeignClients
@SpringBootApplication
public class ConsumerDeptFeign80 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerDeptFeign80.class, args);
    }

}
