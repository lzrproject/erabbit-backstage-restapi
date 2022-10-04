package com.gateway.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewaySysApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewaySysApplication.class,args);
    }

    /***
     * 使用ip作为标识，根据ip限流
     */
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver(){
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //获取当前的ip地址
                String ip = exchange.getRequest().getRemoteAddress().getHostName();
                return Mono.just(ip);
            }
        };
    }
}
