package com.erabbit.oauth2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.oauth2.service.UserLoginService;
import com.erabbit.oauth2.util.AuthToken;
import com.erabbit.user.feign.UserFeign;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private UserLoginService userLoginService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

//    @Value("${auth.cookieMaxAge}")
//    private int cookieMaxAge;
//
//    @Value("${auth.cookieDomain}")
//    private String cookieDomain;

    @GetMapping("main")
    public String main(){
        return "login";
    }

    /**
     * 用户登录
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("admin")
    @ResponseBody
    public Result login(@RequestParam String username,@RequestParam String password) throws Exception {
//        String username = userDetails.getUsername();
//        String password = userDetails.getPassword();

        Result<User> userAll = userFeign.findUsername(username);
        User data = userAll.getData();
        if (data == null){
            return new Result(false, StatusCode.ERROR_PRIVILEGE,"登录失败");
        }

        if (data.getEnable() != 1) return new Result(false, StatusCode.ERROR_PRIVILEGE,"账号未启用，请联系管理员");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(password, data.getPassword());

        if (!matches) {
            return new Result(false, StatusCode.ERROR_PRIVILEGE,"登录失败");
        }

        AuthToken authToken = userLoginService.login(username, password, clientId, clientSecret, "password");
        //token存入cookie
//        if (authToken != null) {
//            saveCookie(authToken.getAccessToken());
//        }
        return new Result(true, StatusCode.SUCCESS,"登录成功",authToken);
    }

    /***
     * 将令牌存储到cookie
     * @param token
     */
    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
//        CookieUtil.addCookie(response,cookieDomain,"/","Authorization",token,cookieMaxAge,false);
    }

    @GetMapping("register")
    public Result register(@RequestParam("username") String username,@RequestParam("password") String password){

        Result<User> user = userFeign.findUsername(username);
        User data = user.getData();
        if (data != null){
            return new Result(false, StatusCode.SUCCESS,"用户名存在");
        }
        return null;
    }

    public static void main(String[] args) {
        JSONObject jsonObject = JSON.parseObject("{\"connectInfo\":{\"password\":\"lanlanfox\",\"jdbcUrl\":\"jdbc:mysql://119.91.211.99:3306/cooper?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true\",\"driverClassName\":\"com.mysql.cj.jdbc.Driver\",\"username\":\"root\"},\"sql\":\"select id,name from date\",\"fields\":[\"id\",\"name\"],\"className\":\"cn.ubattery.connector.mysqlconnector.service.impl.FlinkDataSource\"}");
        System.out.println("-configParams " +jsonObject.toJSONString());
    }
}
