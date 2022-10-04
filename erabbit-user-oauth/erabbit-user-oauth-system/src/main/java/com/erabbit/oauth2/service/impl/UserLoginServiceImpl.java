package com.erabbit.oauth2.service.impl;

import com.erabbit.oauth2.service.UserLoginService;
import com.erabbit.oauth2.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class UserLoginServiceImpl implements UserLoginService {


    //实现请求发送
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    //登录
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) throws Exception {


        //获取指定服务的注册服务
        ServiceInstance choose = loadBalancerClient.choose("service-sys-oauth");

//        //调用请求地址
        String url = choose.getUri()+"/oauth/token";
//        String url = "http://localhost:9001/oauth/token";

        //数据封装
        MultiValueMap<String,String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username",username);
        bodyMap.add("password",password);
        bodyMap.add("grant_type",grant_type);

        //请求头封装
        String Authorization = "Basic "+ new String(Base64.getEncoder().encode((clientId+":"+clientSecret).getBytes()),"UTF-8");
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap();
        headerMap.add("Authorization",Authorization);

        HttpEntity httpEntity = new HttpEntity(bodyMap,headerMap);

        /***
         * 4xx的状态吗会抛出HttpClientErrorException；
         * 5xx的状态码会抛出HttpServerErrorException。
         * 这也就是我们一开始遇到的问题的原因所在了。而在handleError中，
         * 执行了response.getBody()，这就导致我们后续获取不到响应体了，
         * 如果要获取的话，需要进行自定义相关处理。
         *
         * execute()参数
         * 1.请求地址  2.提交方式  3.requestEntity：请求提交数据封装（请求体，请求头）  4.responseType：返回数据需要转换的类型
         */
        ResponseEntity<Map> map = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        

//        System.out.println(map);


        //登录后的令牌信息
        Map<String,String> exMap= map.getBody();

        AuthToken authToken = new AuthToken();
        //访问令牌
        String accessToken = (String) exMap.get("access_token");
        //刷新令牌
        String refreshToken = (String) exMap.get("refresh_token");
        //jti，作为用户的身份标识
        String jwtToken= (String) exMap.get("jti");

        authToken.setJti(jwtToken);
        authToken.setAccessToken(accessToken);
        authToken.setRefreshToken(refreshToken);

        return authToken;
    }
}
