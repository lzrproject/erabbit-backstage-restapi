package com.erabbit.seckill.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Merge {
    private String skuId;
    private boolean selected;
    private Integer count;

    private String[] skuIds;
    private String time;
    private Long seckillId;
    private Integer addressId;
    private List goods;

    private Integer pageNum;
    private Integer pageSize;
    private Integer orderState;

    private String username;

    private String cancelReason;    //取消订单

}
