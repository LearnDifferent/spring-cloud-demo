package com.example.springcloud.alibaba.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Sentinel 自定义异常处理结果
 *
 * @author zhou
 * @date 2024/6/4
 */
@Component
public class SentinelBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       BlockException e) throws Exception {
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        String msg = "未知异常";
        if (e instanceof AuthorityException) {
            msg = "授权规则异常，没有权限";
            status = HttpStatus.UNAUTHORIZED;
        }
        if (e instanceof FlowException) {
            msg = "限流异常";
        }
        if (e instanceof DegradeException) {
            msg = "降级异常";
        }
        if (e instanceof ParamFlowException) {
            msg = "热点参数限流异常";
        }
        if (e instanceof SystemBlockException) {
            msg = "系统规则异常";
        }

        // Return Response
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(status.value());
        httpServletResponse.getWriter().println("{\"status\": " + status.value()
                + ", \"message\": \"" + msg + "\"}");
    }
}
