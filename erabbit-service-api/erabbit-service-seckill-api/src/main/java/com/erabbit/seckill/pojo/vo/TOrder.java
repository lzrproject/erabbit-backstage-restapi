package com.erabbit.seckill.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class TOrder {
    private List<Item> items;
    private Integer counts;
    private Integer page;   //页码
    private Integer pageSize;//每页数量
    private Integer pages;  //最后一页
}
