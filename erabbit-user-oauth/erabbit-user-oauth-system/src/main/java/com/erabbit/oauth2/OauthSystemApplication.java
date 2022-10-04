package com.erabbit.oauth2;

import com.erabbit.oauth2.interceptor.TokenRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthorizationServer
@EnableFeignClients(basePackages = {"com.erabbit.user"})
public class OauthSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthSystemApplication.class,args);
    }

    /***
     * 创建拦截器Bean对象
     * @return
     */
//    @Bean
//    public TokenRequestInterceptor feignInterceptor(){
//        return new TokenRequestInterceptor();
//    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
