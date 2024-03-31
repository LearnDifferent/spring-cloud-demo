package com.example.springcloud.alibaba.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.springcloud.alibaba.entity.Goods;
import com.example.springcloud.alibaba.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * Goods service
 *
 * @author zhou
 * @date 2024/3/31
 */
@Service
public class GoodServiceImpl implements GoodsService {

    /**
     * 使用 {@link SentinelResource} 注解，将这个 service 方法设定为一个可以被监控的资源
     *
     * @param id goods id
     * @return goods info
     */
    @Override
    @SentinelResource("goods")
    public Goods getDetail(int id) {
        return new Goods(id, "nothing");
    }
}