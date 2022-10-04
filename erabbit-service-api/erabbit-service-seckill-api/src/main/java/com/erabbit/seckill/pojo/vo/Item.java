package com.erabbit.seckill.pojo.vo;

import com.erabbit.seckill.pojo.TSku;
import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long countdown;
    private String createTime;
    private String payLatestTime;
    private String id;
    private Integer orderState; //订单状态
    private Integer payChannel;
    private String payMoney;
    private Integer payType;
    private Integer postFee;
    private List<TSku> skus;
    private String totalMoney;
    private Integer totalNum;
}
