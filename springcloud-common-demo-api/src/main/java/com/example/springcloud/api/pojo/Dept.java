package com.example.springcloud.api.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Dept implements Serializable {

    private Long deptno;
    private String dname;

    // 因为相同的数据，可能会存在不同的数据库中，所以需要该数据的数据库名称
    private String db_source;

    public Dept(String dname) {
        this.dname = dname;
    }
}
