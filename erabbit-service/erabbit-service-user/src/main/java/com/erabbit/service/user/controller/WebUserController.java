package com.erabbit.service.user.controller;

import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.service.user.config.TokenDecode;
import com.erabbit.service.user.service.UserService;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("webUser")
public class WebUserController {
    @Autowired
    private UserService userService;

    @GetMapping("getUserInfo")
    public UserInfo getUserInfo(){
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        UserInfo user = new UserInfo();
        user.setId(Integer.parseInt(userInfo.get("id")));
        user.setAccount(userInfo.get("username"));
        user.setAvatar(userInfo.get("avatar"));
        user.setMobile(userInfo.get("avatar"));
        user.setNickname(userInfo.get("avatar"));
        return user;
    }
}
