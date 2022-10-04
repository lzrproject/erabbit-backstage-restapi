package com.erabbit.service.user.controller;


import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.service.user.service.UserService;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.UserRole;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/user")
@Slf4j
//@DefaultProperties(defaultFallback = "userGlobalFallback")  //全局降级
public class UserController {
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("erabbit"));
        System.out.println(bCryptPasswordEncoder.matches("erabbit","$2a$10$bql0Se4BJEUCouFIrgCHlOhjne1KI5RhZMYe031hJGACzW0FV8Eye"));
    }

    @PostMapping(value = "/add")
    public Result addAll(@RequestBody User user) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        List<User> userList = userService.listByMap(map);

        if (userList.size() != 0) return new Result(false,StatusCode.SUCCESS,"用户名重复,请重新输入");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try{
            userService.save(user);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleIds(user.getRoleIds());
            userService.addRoleAndUser(userRole);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR_PARAMS,"输入数据有误");
        }

        return new Result(true, StatusCode.SUCCESS,"添加成功");
    }

    /**
     * 前台注册
     * @param user
     * @return
     */
    @PostMapping(value = "/pcAdd")
    public Result pcAdd(@RequestBody User user) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        List<User> userList = userService.listByMap(map);

        if (userList.size() != 0) return new Result(false,StatusCode.SUCCESS,"用户名重复,请重新输入");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.save(user);
        return new Result(true, StatusCode.SUCCESS,"添加成功");
    }

    @GetMapping("findUsername/{username}")
    @HystrixCommand(fallbackMethod = "findUserFallbackHandler",commandProperties = {
            //表示如果调用paymentInfoTimeout方法，响应的时间超出5000毫秒（5s）就会触发降级方法，
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    },threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "30"),
            @HystrixProperty(name = "maxQueueSize", value = "100"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100")
    })
    public Result<User> findUsername(@PathVariable("username") String username){
        log.info("查询用户：{}",username);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        User user = userService.findUsername(username);
        return new Result<>(true,StatusCode.SUCCESS,"查询成功",user);
    }

    /**
     * 查询用户基本信息、角色、权限
     * @param username
     * @return
     */
    @GetMapping("findUsernameDetails/{username}")
    public Result<User> findUsernameDetails(@PathVariable("username") String username){
        User user = userService.findUsernameDetails(username);
        return new Result(true,StatusCode.SUCCESS,"查询成功",user);
    }

    @PostMapping("/page")
    public Result<PageInfo> findPage(@RequestBody PageSelect pageSelect){
        PageInfo<User> result = userService.findPage(pageSelect);
        return new Result(true,StatusCode.SUCCESS,"查询成功",result);
    }

    @GetMapping(value = {"/byId"})
    public Result<User> findById(@RequestParam("id") Integer id){
        User result = userService.getById(id);
        return new Result<User>(true,StatusCode.SUCCESS,"查询成功",result);
    }

    @GetMapping("findRoleByUid/{uid}")
    public Result<List<Role>> findRoleByUid(@PathVariable("uid") Integer uid){
        List<Role> result = userService.findRoleByUid(uid);
//        result.stream().map(role -> {
//            role.setChecked(role.getChecked() == "1"?tr);
//        });
        return new Result(true,StatusCode.SUCCESS,"查询成功",result);
    }

    @PutMapping("/edit")
    public Result update(@RequestBody User user){
        userService.update(user);
        return new Result(true,StatusCode.SUCCESS,"修改成功");
    }

    @PutMapping("/editEnable")
    public Result editEnable(@RequestBody User user){
        userService.updateById(user);
        return new Result(true,StatusCode.SUCCESS,"修改成功");
    }

    @DeleteMapping("del")
    public Result delete(@RequestParam("id") Integer id){
        userService.delete(id);
        return new Result(true,StatusCode.SUCCESS,"删除成功");
    }


    ////////////////////////////////////////////////////////////

//    @HystrixCommand
    public String userGlobalFallback(){
        return "this is userGlobalFallback(全局的降级方法)";
    }

    public Result<User> findUserFallbackHandler(String username){
        log.info("UserController findUsername局部降级：",username);
        return new Result<>(false,StatusCode.ERROR_RESPONSE,"查询超时",null);
    }
}

