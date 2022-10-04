package com.erabbit.oauth2.interceptor;

import com.erabbit.oauth2.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenRequestInterceptor implements RequestInterceptor {
    @Autowired
    private AdminToken adminToken;

    /***
     * 拦截器 Feign执行之前拦截，获取登录令牌
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //生成令牌 放入头文件

        String token = adminToken.adminToken();
        requestTemplate.header("Authorization","bearer "+token);

    }
}
