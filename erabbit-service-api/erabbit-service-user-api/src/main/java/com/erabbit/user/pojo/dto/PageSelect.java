package com.erabbit.user.pojo.dto;

import lombok.Data;

@Data
public class PageSelect {
    /**
     * 当前页
     * */
    private Integer page;

    /**
     * 每页数量
     * */
    private Integer limit;

    private String username;

    private String roleName;
    private String roleCode;
}
