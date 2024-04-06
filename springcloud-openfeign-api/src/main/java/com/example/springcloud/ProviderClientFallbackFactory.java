package com.example.springcloud;

import feign.hystrix.FallbackFactory;

/**
 * Provider Client Fallback Factory
 * 使用 Fallback Factory 比较直接 Fallback 好，因为有 {@link Throwable} 可以处理错误信息。
 * <p>
 * 需要在 {@link ProviderClientConfig} 配置类中将其注册为 Bean 才行，直接写 {@code Component} 注解并不会生效。
 * </p>
 *
 * @author zhou
 * @date 2024/4/5
 * @see ProviderClientConfig
 */
public class ProviderClientFallbackFactory implements FallbackFactory<ProviderClient> {

    @Override
    public ProviderClient create(Throwable throwable) {
        // 创建 ProviderClient 接口的匿名内部实现类
        // 这个实现类 实现的是 Fallback 降级的逻辑
        return new ProviderClient() {

            /**
             * {@link ProviderClient#providerApi()} 方法的 Fallback
             *
             * @return fallback 的数据
             */
            @Override
            public String providerApi() {
                String message = throwable.getMessage();
                return "ProviderClient#providerApi() - Fallback - Error Message：【" + message + "】";
            }
        };
    }
}