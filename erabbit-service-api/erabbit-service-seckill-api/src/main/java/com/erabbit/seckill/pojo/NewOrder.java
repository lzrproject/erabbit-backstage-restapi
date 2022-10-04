package com.erabbit.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.erabbit.user.pojo.Address;
import lombok.*;

import javax.persistence.Transient;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    /**
     * 数量合计
     */
    private Integer totalNum;

    /**
     * 金额合计
     */
    private Integer totalMoney;

    /**
     * 优惠金额
     */
    private Integer preMoney;

    /**
     * 邮费
     */
    private Integer postFee;

    /**
     * 实付金额
     */
    private Integer payMoney;

    /**
     * 支付类型，1、在线支付、0 货到付款
     */
    private String payType;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 付款时间
     */
    private LocalDateTime payLatestTime;

    /**
     * 发货时间
     */
    private LocalDateTime consignTime;

    /**
     * 交易完成时间
     */
    private LocalDateTime endTime;

    /**
     * 交易关闭时间
     */
    private LocalDateTime closeTime;

    /**
     * 订单状态,1:待付款、2:待发货、3:待收货、4:待评价、5:已完成、6:已取消，未传该参数或0为全部
     */
    private Integer orderState;

    private String payChannel;

    /**
     * 发货状态,0:未发货，1：已发货，2：已收货
     */
    private String consignStatus;

    /**
     * SKU_ID
     */
    private Integer addressId;

    /**
     * 是否删除
     */
    private String isDelete;

    private List<TSku> skus;

    @Transient
    private transient Address address;

}
