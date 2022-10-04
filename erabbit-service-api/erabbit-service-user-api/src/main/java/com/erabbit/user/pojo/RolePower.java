package com.erabbit.user.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_role_power")
public class RolePower {

    /**
     * 映射编号
     * */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 角色编号
     * */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 权限编号
     * */
    @Column(name = "power_id")
    private Integer powerId;

}
