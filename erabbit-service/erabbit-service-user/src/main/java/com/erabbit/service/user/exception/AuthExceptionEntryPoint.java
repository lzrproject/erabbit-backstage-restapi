package com.erabbit.service.user.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException {

        Throwable cause = authException.getCause();

        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//        response.setHeader("Content-Type", "text/html;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Result result = new Result();
        try {
            if (cause instanceof InvalidTokenException) {
//                response.setStatus(HttpStatus.NOT_FOUND.value());
                result.setFlag(false);
                result.setCode(StatusCode.ERROR_PERMISSION);
                result.setMessage("token失效");
                response.getWriter().write(JSON.toJSONString(result));
            }else {
                result.setFlag(false);
                result.setCode(StatusCode.ERROR_PERMISSION);
                result.setMessage(authException.getMessage());
                response.getWriter().write(JSON.toJSONString(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}