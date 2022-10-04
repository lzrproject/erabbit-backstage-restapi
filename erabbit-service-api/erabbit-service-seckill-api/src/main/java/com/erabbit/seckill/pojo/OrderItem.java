package com.erabbit.seckill.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * orderItem实体类
 * @author 黑马架构师2.5
 *
 */
@Data
public class OrderItem implements Serializable {

	@Id
	private String id;//ID


	
	private Integer categoryId1;//1级分类
	private Integer categoryId2;//2级分类
	private Integer categoryId3;//3级分类
	private String spuId;//SPU_ID
	private String skuId;//SKU_ID
	private String orderId;//订单ID
	private String name;//商品名称
	private Integer price;//单价
	private Integer num;//数量
	private Integer money;//总金额
	private Integer payMoney;//实付金额
	private String image;//图片地址
	private Integer weight;//重量
	private Integer postFee;//运费
	private String isReturn;//是否退货


	private String attrsText;
	private boolean isCollect;
	private boolean isEffective = true;//有效标识
	private String nowOriginalPrice;//原价
	private String nowPrice;//现价
	private List specs;
	private Integer stock;//库存
	private boolean selected;//是否选中


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
