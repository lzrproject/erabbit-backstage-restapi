package com.erabbit.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * spec实体类
 *
 */
@Data
@TableName("tb_spec")
public class Spec implements Serializable {

	@Id
	private Integer id;//ID

	private String name;//名称
	private String options;//规格选项
	private Integer seq;//排序
	private Integer templateId;//模板ID

	private String valueName;

}
