package com.example.springcloud.alibaba.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Goods
 *
 * @author zhou
 * @date 2024/3/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {

    private Integer id;
    private String description;
}