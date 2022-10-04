package com.erabbit.user.feign;

import com.erabbit.common.entity.Result;
import com.erabbit.user.pojo.Menu;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@FeignClient(value = "service-user")
@RequestMapping(value = "/power")
public interface PowerFeign {
    /**
     * Describe: 获取权限列表数据
     * Param ModelAndView
     * Return 权限列表数据
     * */
    @PostMapping("/list")
    Result<List<Power>> data(@RequestBody Power power);

    /**
     * 权限添加数据
     */
    @PostMapping("/add")
    Result addAll(@RequestBody Power power);

    /**
     * 根据id查询数据
     */
    @GetMapping("/byId")
    Result<Power> findById(@RequestParam("id") Integer id);

    /**
     * Describe: 修改权限信息
     * Param SysPower
     * Return 执行结果
     * */
    @PutMapping("/updated")
    Result update(@RequestBody Power power);

    /**
     * Describe: 根据 id 进行删除
     * Param id
     * Return ResuTree
     * */
    @DeleteMapping("delete/{id}")
    Result remove(@PathVariable("id") String id);


    @GetMapping("/menu")
    public List<Menu> getUserMenu();

    @GetMapping("getUser")
    public User getUser();
}
