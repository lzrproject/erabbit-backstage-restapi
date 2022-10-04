package com.erabbit.seckill.pojo;

import lombok.Data;

@Data
public class Goods {
    private String id;
    private String price;
    private String picture;
    private String name;
    private Integer orderNum;
    private Integer discount;
    private String desc;
}
