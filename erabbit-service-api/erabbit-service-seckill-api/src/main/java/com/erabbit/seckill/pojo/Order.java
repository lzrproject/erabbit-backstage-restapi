package com.erabbit.seckill.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Order implements Serializable {

    private String id;

    private String name;
    private String skuId;
    private String picture;
    private String postFee;//运费
    private String discount;//折扣
    private Integer count;
    private String attrsText;
    private boolean isCollect;
    private boolean isEffective = true;//有效标识
//    private String nowOriginalPrice;//原价
//    private String nowPrice;//现价
//    private String price;//价格
    private List specs;
    private Integer stock;//库存
    private boolean selected;//是否选中

    private String payPrice;//折后价格（单）
    private String price;//单价
    private String totalPayPrice;//折后实付总价
    private String totalPrice;//折后总价



    public boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public boolean getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(boolean isEffective) {
        this.isEffective = isEffective;
    }
}
