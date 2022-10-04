package com.erabbit.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.user.feign.RoleFeign;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色、权限管理
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController {
    /**
     * Describe: 基础路径
     * */
    private static String MODULE_PATH = "system/role/";

    @Autowired
    private RoleFeign roleFeign;

    /**
     * Describe: 获取角色列表视图
     * Param ModelAndView
     * Return 用户列表视图
     * */
    @GetMapping("main")
//    @PreAuthorize("hasAuthority('sys:role:main')")
    public ModelAndView main(){
        return new Result().jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取角色列表数据
     * Param SysRole PageDomain
     * Return 角色列表数据
     * */
    @GetMapping("data")
//    @PreAuthorize("hasAuthority('sys:role:data')")
    public JSONObject data(PageSelect pageSelect){
        Result<PageInfo> result = roleFeign.findPage(pageSelect);
        if(result.isFlag()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",0);
            jsonObject.put("message",result.getMessage());
            jsonObject.put("count",result.getData().getTotal());
            jsonObject.put("data",result.getData().getList());
            return jsonObject;
        }
        return null;
    }



    /**
     * Describe: 获取角色新增视图
     * Param ModelAndView
     * Return 角色新增视图
     * */
    @GetMapping("add")
//    @PreAuthorize("hasAuthority('sys:role:add')")
    public ModelAndView add(){
        return new Result().jumpPage(MODULE_PATH + "add");
    }

    /**
     * Describe: 保存角色信息
     * Param SysRole
     * Return 执行结果
     * */
    @PostMapping("save")
//    @PreAuthorize("hasAuthority('sys:role:add')")
    public Result save(@RequestBody Role role){
        Result result = roleFeign.addAll(role);
        return result;
    }

    /**
     * Describe: 获取角色修改视图
     * Param ModelAndView
     * Return 角色修改视图
     * */
    @GetMapping("edit")
//    @PreAuthorize("hasAuthority('sys:role:edit')")
    public ModelAndView edit(ModelAndView modelAndView,@RequestParam Integer id){
        Result<Role> result = roleFeign.findById(id);
        if (result.isFlag()) {
            modelAndView.addObject("sysRole",result.getData());
            modelAndView.setViewName(MODULE_PATH + "edit");
            return modelAndView;
        }
        return null;
    }

    /**
     * Describe: 修改角色信息
     * Param SysRole
     * Return 执行结果
     * */
    @PutMapping("update")
//    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result update(@RequestBody Role role){
        Result result = roleFeign.update(role);
        return result;
    }

    /**
     * Describe: 获取角色授权视图
     * Param ModelAndView
     * Return ModelAndView
     * */
    @GetMapping("power")
//    @PreAuthorize("hasAuthority('sys:role:power')")
    public ModelAndView power(Model model,@RequestParam Integer id){
        model.addAttribute("id",id);
        return new Result().jumpPage(MODULE_PATH + "power");
    }

    /**
     * Describe: 获取角色权限
     * Param RoleId
     * Return ResuTree
     * */
    @GetMapping("getRolePower")
//    @PreAuthorize("hasAuthority('sys:role:power')")
    public JSONObject getRolePower(Integer id){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("code",200);
        jsonObject1.put("message","默认");

//        List<Power> rolePower = roleFeign.getRolePower(id);
        jsonObject.put("data",roleFeign.getRolePower(id));
        jsonObject.put("status",jsonObject1);
        return jsonObject;
    }

    /**
     * Describe: 保存角色权限数据
     * Param: roleId powerIds
     * Return: 执行结果
     * */
    @PutMapping("saveRolePower")
//    @PreAuthorize("hasAuthority('sys:role:power')")
    public Result saveRolePower(@RequestParam Integer roleId, @RequestParam String powerIds) {

        return roleFeign.saveRolePower(roleId,powerIds);
    }

    /**
     * Describe: 删除角色
     * Param: id
     * Return: ResuBean
     * */
    @DeleteMapping("remove/{id}")
//    @PreAuthorize("hasAuthority('sys:role:remove')")
    public Result remove(@PathVariable Integer id){
        Result result = roleFeign.remove(id);
        return result;
    }

//    /**
//     * Describe: 批量删除角色
//     * Param: ids
//     * Return: ResuBean
//     * */
//    @DeleteMapping("batchRemove/{ids}")
//    @PreAuthorize("hasAuthority('sys:role:remove')")
//    public Result batchRemove(@PathVariable String ids){
//        List<String> strings = Arrays.asList(ids.split(","));
//        List<Integer> integers = strings.stream().map(Integer::parseInt).collect(Collectors.toList());
//        Result result = roleFeign.batchRemove(integers);
//        return result;
//    }

    /**
     * Describe: 根据 Id 启用角色
     * Param: roleId
     * Return: ResuBean
     * */
    @PutMapping("enable")
    public Result enable(@RequestBody Role role){
        role.setEnable(1);
        Result result = roleFeign.update(role);
        return result;
    }

    /**
     * Describe: 根据 Id 禁用角色
     * Param: roleId
     * Return: ResuBean
     * */
    @PutMapping("disable")
    public Result disable(@RequestBody Role role){
        role.setEnable(0);
        Result result = roleFeign.update(role);
        return result;
    }
}
