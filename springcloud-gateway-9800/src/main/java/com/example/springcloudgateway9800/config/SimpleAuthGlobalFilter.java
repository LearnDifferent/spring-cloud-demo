package com.example.springcloudgateway9800.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Spring Cloud Gateway 的 Global Filters 的示例
 *
 * @author zhou
 * @date 2024/3/29
 */
@Component
@Order(-1)
public class SimpleAuthGlobalFilter implements GlobalFilter {

    /**
     * Global Filter 过滤逻辑
     *
     * @param exchange 请求的上下文，用于获取 Request 和 Response 等信息
     * @param chain    Global Chain，主要用于将请求传递到下一个过滤器中
     * @return 返回当前过滤器业务处理的结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求参数
        MultiValueMap<String, String> params = request.getQueryParams();
        // 获取参数中第一个 auth 的参数
        String authParam = "auth";
        String auth = params.getFirst(authParam);
        // 判断参数值是否可以放行，这里简单判断是否为 admin
        String authValue = "admin";
        if (authValue.equalsIgnoreCase(auth)) {
            // 放行，将请求传递到下个过滤器
            return chain.filter(exchange);
        }

        // 如果不可以放行，就拦截
        // 为了用户体验，建议设置一下 Response 的状态码
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        System.out.println("[SimpleAuthGlobalFilter] 拦截请求。请在请求中添加此参数：?"
                + authParam + "=" + authValue);

        // 最后，将 Response 直接标记为 Complete，在这个过滤器中拦截请求
        return response.setComplete();
    }
}
