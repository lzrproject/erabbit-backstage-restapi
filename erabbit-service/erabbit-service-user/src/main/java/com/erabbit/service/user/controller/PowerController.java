package com.erabbit.service.user.controller;


import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.service.user.config.TokenDecode;
import com.erabbit.service.user.service.PowerService;
import com.erabbit.user.pojo.Menu;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/power")
@CrossOrigin
public class PowerController {
    @Autowired
    private PowerService powerService;

    /**
     * Describe: 获取权限列表数据
     * Param ModelAndView
     * Return 权限列表数据
     * */
    @PostMapping("/list")
    public Result<List<Power>> data(@RequestBody Power power){
        List<Power> list = powerService.findAll(power);
        return new Result<List<Power>>(true, StatusCode.SUCCESS,"查询成功",list);
    }

    /**
     * 权限添加数据
     */
    @PostMapping("/add")
    public Result addAll(@RequestBody Power power){
        powerService.save(power);
        return new Result(true,StatusCode.SUCCESS,"添加成功");
    }

    /**
     * 根据id查询数据
     */
    @GetMapping("/byId")
    public Result<Power> findById(@RequestParam("id") Integer id){
        Power result = powerService.getById(id);
        return new Result<Power>(true,StatusCode.SUCCESS,"查询成功",result);
    }

    /**
     * Describe: 修改权限信息
     * Param SysPower
     * Return 执行结果
     * */
    @PutMapping("/updated")
    public Result update(@RequestBody Power power) {
        powerService.update(power);
        return new Result(true,StatusCode.SUCCESS,"修改成功");
    }

    /**
     * Describe: 根据 id 进行删除
     * Param id
     * Return ResuTree
     * */
    @DeleteMapping("delete/{id}")
    public Result remove(@PathVariable("id") String id){
        Power power = new Power();
        power.setParentId(id);
        List<Power> powerList = powerService.findAll(power);
        if(powerList.size() != 0){
            return new Result(false,StatusCode.ERROR_PARAMS,"请先删除子集");
        }
        powerService.remove(id);
        return new Result(true,StatusCode.SUCCESS,"删除成功");
    }

    @GetMapping("/menu")
    public List<Menu> getUserMenu(){
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String uid = userInfo.get("id");
        List<Menu> result = powerService.getUserMenu(Integer.parseInt(uid));
        return result;
    }

    @GetMapping("getUser")
    public User getUser(){
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        User user = new User();
        user.setId(Integer.parseInt(userInfo.get("id")));
        user.setUsername(userInfo.get("username"));
        user.setAvatar(userInfo.get("avatar"));
        return user;
    }
}

