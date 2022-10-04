package com.erabbit.oauth2.exception;

import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException {
        Map<String, Object> map = new HashMap<String, Object>();
        Throwable cause = authException.getCause();

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            if(cause instanceof InvalidTokenException) {
                Result result = new Result(false, StatusCode.ERROR_PERMISSION, "token失效");
                response.getWriter().write(result.toString());
            }else{
                Result result = new Result(false, StatusCode.ERROR_PERMISSION, "token缺失");
                response.getWriter().write(result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
