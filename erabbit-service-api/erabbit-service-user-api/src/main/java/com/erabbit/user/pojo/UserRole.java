package com.erabbit.user.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_user_role")
public class UserRole {
    /**
     * 映射标识
     * */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 用户编号
     * */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 角色编号
     * */
    @Column(name = "role_id")
    private String roleIds;

}
