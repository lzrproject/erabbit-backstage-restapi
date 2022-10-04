package com.erabbit.user.pojo.vo;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;
    private String account;
    private String avatar;
    private String mobile;
    private String nickname;
}
