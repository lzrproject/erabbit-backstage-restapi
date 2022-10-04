package com.erabbit.oauth2.controller.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.common.entity.servlet.ServletUtil;
import com.erabbit.common.entity.string.StringUtil;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 登 录 验 证 码 过 滤 器
 * @author John Ming
 * @createTime 2020/11/20
 */
@Component
public class SecureCaptchaSupport extends OncePerRequestFilter implements Filter {

    private String defaultFilterProcessUrl = "/login/admin";
    private String method = "POST";

    /**
     * 验 证 码 校 监 逻 辑
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (method.equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {
            //通过getInputStream流获取post参数
//            StringBuffer buffer = new StringBuffer();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ServletUtil.getRequest().getInputStream()));
//            String link;
//            String captcha = null;
//
//            try{
//                while ((link = bufferedReader.readLine()) != null){
//                    buffer.append(link);
//                }
//                JSONObject jsonObject = JSONObject.parseObject(buffer.toString());
//                captcha = jsonObject.get("captcha").toString();
//            }catch (IOException e){
//                e.printStackTrace();
//            }

            String captcha = ServletUtil.getRequest().getParameter("captcha");
            response.setContentType("application/json;charset=UTF-8");
            if (StringUtil.isEmpty(captcha)){
                response.getWriter().write(JSON.toJSONString(new Result(false, StatusCode.ERROR_RESPONSE,"验证码不能为空!")));
                return;
            }
            if (!CaptchaUtil.ver(captcha,ServletUtil.getRequest())){
                response.getWriter().write(JSON.toJSONString(new Result(false, StatusCode.ERROR_RESPONSE,"验证码错误!")));
                return;
            }
        }
        chain.doFilter(request, response);
    }
}