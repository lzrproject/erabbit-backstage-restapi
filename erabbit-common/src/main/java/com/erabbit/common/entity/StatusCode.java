package com.erabbit.common.entity;

/**
 * 返回码
 */
public class StatusCode {
//    public static final int OK = 20000;//成功
//    public static final int ERROR = 50000;//失败
//    public static final int LOGINERROR = 20002;//用户名或密码错误
//    public static final int ACCESSERROR = 20003;//权限不足
//    public static final int REMOTEERROR = 20004;//远程调用失败
//    public static final int REPERROR = 20005;//重复操作
//    public static final int NOTFOUNDERROR = 20006;//没有对应的抢购数据


    /**
     * 大湾区
     */
    public static final int SUCCESS = 2000; //接口响应成功

    public static final int ERROR_RESPONSE = 5000; //接口内部错误
    public static final int ERROR_PARAMS = 5002; //参数错误
    public static final int ERROR_PRIVILEGE = 5003; //认证失败
    public static final int ERROR_PERMISSION = 5004; //无权限
    public static final int ERROR_NO_LOGIN = 5006; //未登录

}
