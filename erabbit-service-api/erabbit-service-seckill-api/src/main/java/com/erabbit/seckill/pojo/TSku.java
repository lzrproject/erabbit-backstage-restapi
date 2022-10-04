package com.erabbit.seckill.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TSku implements Serializable {
    private String id;
    private Integer inventory;
    private String oldPrice;
    private String price;
    private String skuCode;
    private List<Object> specs;

    private String attrsText;
    private Integer curPrice;   //打折价格
    private Integer realPay;   //实际价格
    private Integer quantity;   //数量
    private String image;
    private String name;
    private String spuId;
}
