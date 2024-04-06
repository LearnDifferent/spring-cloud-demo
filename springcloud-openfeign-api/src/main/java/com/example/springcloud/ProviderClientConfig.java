package com.example.springcloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 {@link ProviderClient} 的 Fallback Factory 的配置类。
 * <p>
 * 这个配置类必须写在 {@link ProviderClient} 上 {@code configuration = ProviderClientConfig.class}
 * 才能将 {@link ProviderClientFallbackFactory} 配置到 {@link ProviderClient} 上
 * </p>
 * <p>
 * 实际上 {@link Configuration} 注解是可以不加的，
 * 因为 Spring Cloud OpenFeign 在运行时会自动为使用 {@link FeignClient} 注解的接口生成代理实现，
 * 并将这些代理实现注册为 Bean，使得它们可以像其他 Spring 管理的 Bean 那样被注入和使用。
 * 经过测试，只要 {@link FeignClient} 中的 {@code configuration} 属性添加了当前这个类，确实是不需要 {@link Configuration} 注解的。
 * </p>
 * <p>
 * 即便上面说不需要 {@link Configuration} 注解，但是这个类里面注册 Fallback Factory 的时候就一定要加上 {@link Bean} 注解
 * </p>
 *
 * @author zhou
 * @date 2024/4/5
 */
@Configuration
public class ProviderClientConfig {

    @Bean
    public ProviderClientFallbackFactory providerClientFallbackFactory() {
        return new ProviderClientFallbackFactory();
    }
}
