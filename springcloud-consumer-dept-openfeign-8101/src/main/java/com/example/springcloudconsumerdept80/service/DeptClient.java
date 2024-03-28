package com.example.springcloudconsumerdept80.service;

import com.example.springcloud.api.pojo.Dept;
import com.example.springcloudconsumerdept80.ConsumerDeptFeign8101;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 记得在当前接口上，添加注解 @Component（或 @Service）。
 * 如果没有添加 @Component ，就需要在 {@link ConsumerDeptFeign8101 主启动类} 上
 * 使用 @EnableFeignClients 注解，然后再使用该注解的 basePackageClasses 属性扫描到这个类。
 * <p>
 * Declare a Feign client using @FeignClient，
 * value 标明 provider 的 application name (service id)
 * <p>
 * fallbackFactory 或 fallback 属性可以定义 fall back 后的处理类：
 * - fallback 是直接实现"原方案接口"的替代方案，参考 {@link DeptClientFallback_Simple}
 * - fallbackFactory 则是实现 FallbackFactory<原方案接口> 的替代方案，参考 {@link DeptClientFallback}
 * <p>
 * 使用了 Hystrix 的 fall back 模式后，消费者即使在客户端停止服务后，也能返回替代方案的 json 数据
 */

//@FeignClient(value = "springcloud-provider-dept", fallback = DeptClientFallback_Simple.class)
@Component
@FeignClient(value = "springcloud-provider-dept", fallbackFactory = DeptClientFallback.class)
public interface DeptClient {

    // 在接口上，直接调用 provider
    // we can use the Spring Web annotations to
    // declare the APIs that we want to reach out to

    @GetMapping("/dept/find/{id}")
    Dept findDeptById(@PathVariable("id") Long id);

    @GetMapping("/dept/find")
    List<Dept> findAllDepts();

    @PostMapping("/dept/add")
    Boolean addDept(@RequestBody Dept dept);
}

