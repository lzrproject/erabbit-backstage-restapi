package com.erabbit.seckill.pojo.vo;

import lombok.Data;

@Data
public class Summary {
    private Integer discountPrice;  //折扣价格
    private Integer goodsCount; //商品价格
    private Integer postFee;    //运费
    private String totalPayPrice;  //应付总额
    private String totalPrice; //总价格
}
