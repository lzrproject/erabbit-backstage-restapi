package com.erabbit.oauth2.service;


import com.erabbit.oauth2.util.AuthToken;

public interface UserLoginService {

    //登录
    AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception;
}
