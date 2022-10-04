package com.erabbit.system.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

@Configuration
public class FeignInterceptor implements RequestInterceptor {

    /***
     * 拦截器 Feign执行之前拦截，获取令牌
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        /**
         * 获取用户令牌
         * 将令牌存入头文件中
         */
        try{
            //记录当前用户请求的所有数据，包含请求头和请求参数等
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(requestAttributes != null){
                //获取请求头数据 获取所有头名字
                Enumeration<String> headerNames = requestAttributes.getRequest().getHeaderNames();
                if(headerNames != null){
                    while(headerNames.hasMoreElements()){
                        String headerKey = headerNames.nextElement();
                        String headerValue = requestAttributes.getRequest().getHeader(headerKey);
                        if (headerKey.equals("content-length")) continue;
                        //将请求头信息封装到头，使用feign调用传递给下个微服务
                        requestTemplate.header(headerKey,headerValue);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }







    }
}
