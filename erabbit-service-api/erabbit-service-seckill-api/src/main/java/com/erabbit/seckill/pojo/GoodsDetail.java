package com.erabbit.seckill.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsDetail implements Serializable {
    private String id;  //SpuId

    private List<Category> categories; //商品类目
    private Integer collectCount;   //收藏数量
    private Integer commentCount;   //评论数量
    private String desc;   //描述
    private double discount;//折扣
    private String evaluationInfo;//折扣
    private Integer inventory;//库存
    private boolean isCollect;//是否收藏
    private boolean isPreSale=false;//
    private List<String> mainPictures;//图片
    private String name;//名称
    private String oldPrice;//原价
    private String price;//价格
//    private Integer salesCount;//销售数量

    private List<TSku> skus;
    private List<Object> specs;//规格

    private Integer num;//秒杀数量
    private String startTime;
    private String endTime;
    private String smallPic;
    private Integer SalesPer;
    private String seckillId;

    public boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }
}
