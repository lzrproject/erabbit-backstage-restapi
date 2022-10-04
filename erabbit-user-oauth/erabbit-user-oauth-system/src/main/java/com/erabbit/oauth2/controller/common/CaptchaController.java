package com.erabbit.oauth2.controller.common;

import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Describe: 验证码控制器
 * */
@RestController
@RequestMapping("system/captcha")
public class CaptchaController {
    static{
        System.setProperty("java.awt.headless", "true");
    }

    /**
     * 验证码生成
     * @param request 请求报文
     * @param response 响应报文
     * */
    @RequestMapping("generate")
    public void generate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.out(request, response);
    }

    /**
     * 异步验证
     * @param request 请求报文
     * @param captcha 验证码
     * @return 验证结果
     * */
    @RequestMapping("verify")
    public Result verify(HttpServletRequest request, String captcha){
        if(CaptchaUtil.ver(captcha,request)){
            return new Result(true, StatusCode.SUCCESS,"验证成功");
        }
        return new Result(false,StatusCode.ERROR_RESPONSE,"验证失败");
    }

}
