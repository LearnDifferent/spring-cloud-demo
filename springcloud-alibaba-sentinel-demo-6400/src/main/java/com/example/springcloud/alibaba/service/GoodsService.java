package com.example.springcloud.alibaba.service;

import com.example.springcloud.alibaba.entity.Goods;

/**
 * Goods service
 *
 * @author zhou
 * @date 2024/3/31
 */
public interface GoodsService {

    /**
     * Get goods info by goods id
     *
     * @param id goods id
     * @return goods info
     */
    Goods getDetail(int id);
}