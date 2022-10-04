package com.erabbit.common.exception;


import com.alibaba.fastjson.JSON;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.common.entity.servlet.ServletUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result baseException(Exception e){
        log.info("全局捕获异常："+e.getMessage());
        return new Result(false, StatusCode.ERROR_RESPONSE,e.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public Result feignException(FeignException e,HttpServletRequest request,HttpServletResponse response){
        response.setStatus(e.status());
        log.info("运行时捕获异常：{}",e.getMessage());
        return new Result(false,StatusCode.ERROR_RESPONSE,"运行时异常："+e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeException(RuntimeException e,HttpServletRequest request,HttpServletResponse response){
        response.setStatus(response.getStatus());
        log.info("运行时捕获异常：{}",e.getMessage());
        return new Result(false,StatusCode.ERROR_RESPONSE,"运行时异常："+e.getMessage());
    }
}
