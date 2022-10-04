package com.erabbit.user.feign;

import com.erabbit.common.entity.Result;
import com.erabbit.user.handle.FallbackServiceHandler;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "service-user",fallback = FallbackServiceHandler.class, path = "/user")
//@RequestMapping("user")
public interface UserFeign {

    @PostMapping(value = "/add")
    public Result addAll(@RequestBody User user);

    @GetMapping("findUsername/{username}")
    Result<User> findUsername(@PathVariable("username") String username);   //根据username查询用户基本信息

    @GetMapping("findUsernameDetails/{username}")
    Result<User> findUsernameDetails(@PathVariable("username") String username);    //查询用户基本信息、角色、权限

    @PostMapping("/page")
    public Result<PageInfo> findPage(@RequestBody PageSelect pageSelect); //查询用户列表

    @GetMapping(value = {"/byId"})
    public Result<User> findById(@RequestParam("id") Integer id); //根据id查询用户基本信息

    @GetMapping("findRoleByUid/{uid}")
    public Result<List<Role>> findRoleByUid(@PathVariable("uid") Integer uid);  //根据用户id查询选中角色

    @PutMapping("/edit")
    Result update(@RequestBody User user);

    @PutMapping("/editEnable")
    public Result editEnable(@RequestBody User user);

    @DeleteMapping("del")
    Result delete(@RequestParam("id") Integer id);
}
