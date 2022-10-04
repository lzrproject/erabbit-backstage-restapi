package com.erabbit.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * sku实体类
 * @author erabbit 111
 *
 */
@Data
@TableName("tb_sku")
public class Sku implements Serializable {

	@Id
	private String id;//商品id


	
	private String sn;//商品条码
	private String name;//SKU名称
	private Integer price;//价格（分）
	private Integer num;//库存数量
	private Integer alertNum;//库存预警数量
	private String image;//商品图片
	private String images;//商品图片列表
	private Integer weight;//重量（克）
	private java.util.Date createTime;//创建时间
	private java.util.Date updateTime;//更新时间
	private String spuId;//SPUID
	private Integer categoryId;//类目ID
	private String categoryName;//类目名称
	private String brandName;//品牌名称
	private String spec;//规格
	private Integer saleNum;//销量
	private Integer commentNum;//评论数
	private String status;//商品状态 1-正常，2-下架，3-删除


}
