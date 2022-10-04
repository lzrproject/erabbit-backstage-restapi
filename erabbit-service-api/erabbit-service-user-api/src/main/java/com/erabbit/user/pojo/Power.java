package com.erabbit.user.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_power")
public class Power implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限名称
     */
    private String powerName;

    /**
     * 权限类型
     */
    private String powerType;

    /**
     * 权限标识(分类）
     */
    private String powerCode;

    /**
     * 权限路径
     */
    private String powerUrl;

    /**
     * 打开方式
     */
    private String openType;

    /**
     * 父类编号
     */
    private String parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否开启
     */
    private Integer enable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @Transient
    private transient String checkArr = "0";

}
