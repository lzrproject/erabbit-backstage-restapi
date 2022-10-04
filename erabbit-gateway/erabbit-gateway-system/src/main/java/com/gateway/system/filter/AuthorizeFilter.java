package com.gateway.system.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RefreshScope
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("进入全局过滤器");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        String url = ",";
        String[] splits = url.split(",");
        for (String split : splits) {
            if (path.startsWith(split)) {
                return chain.filter(exchange);
            }
        }

        //获取用户信息
        //1)头文件
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        //2)参数获取令牌
        if(StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        //3)Cookie中
        if(StringUtils.isEmpty(token)){
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
//            HttpCookie httpCookie = request.getCookies().getFirst("JSESSIONID");
            if(httpCookie != null){
                token = httpCookie.getValue();
            }
        }


        //如果令牌为空，不允许访问，直接拦截(令牌前面加bearer)
        if(StringUtils.isEmpty(token)){
            //设置没有权限状态码 401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return noPower(exchange);
            return needAuthorization("http://"+request.getURI().getHost()+":10012/login/admin",exchange);
        }


        if(!token.startsWith("bearer ") && !token.startsWith("Bearer ")){
            token = "bearer "+token;
            request.mutate().header(AUTHORIZE_TOKEN,token);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> noPower(ServerWebExchange serverWebExchange) {
        // 权限不够拦截
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag",false);
        jsonObject.put("errorCode","5004");
        jsonObject.put("errorMsg","无权限");

        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));

    }

    /**
     * 响应设置
     * @param url
     * @param exchange
     * @return
     */
    public Mono<Void> needAuthorization(String url, ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",url);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
