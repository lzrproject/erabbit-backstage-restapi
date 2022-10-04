package com.erabbit.service.user.controller;


import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.service.user.service.RoleService;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 角色分页列表
     * 角色查询列表
     */
    @PostMapping("/page")
    public Result<PageInfo> findPage(@RequestBody PageSelect pageSelect){
        PageInfo<Role> result = roleService.findPage(pageSelect);
        return new Result(true, StatusCode.SUCCESS,"查询成功",result);
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("findAll")
    public Result<List<Role>> findAll(){
        List<Role> result = roleService.list();
        return new Result(true, StatusCode.SUCCESS,"查询成功",result);
    }

    /**
     * 角色添加
     * @param role
     * @return
     */
    @PostMapping("/add")
    public Result addAll(@RequestBody Role role){
        roleService.save(role);
        return new Result(true, StatusCode.SUCCESS,"添加成功");
    }

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @GetMapping("/byId")
    public Result<Role> findById(@RequestParam("id") Integer id){
        Role result = roleService.getById(id);
        return new Result(true, StatusCode.SUCCESS,"查询成功",result);
    }

    /**
     * 修改角色数据
     * @param role
     * @return
     */
    @PutMapping("/updated")
    public Result update(@RequestBody Role role){
        roleService.updateById(role);
        return new Result(true, StatusCode.SUCCESS,"修改成功");
    }

    /**
     * Describe: 查询角色权限信息
     * Param: id
     * Return: 返回角色信息
     * */
    @GetMapping("rolePower")
    public List<Power> getRolePower(@RequestParam("id") Integer id){
        return roleService.getRolePower(id);

    }

    /**
     * Describe: 保存角色权限数据
     * Param: roleId powerIds
     * Return: 执行结果
     * */
    @PutMapping("upRolePower")
    public Result saveRolePower(@RequestParam("roleId") Integer roleId,@RequestParam("powerIds") String powerIds){
        roleService.saveRolePower(roleId,powerIds);
        return new Result(true, StatusCode.SUCCESS,"保存成功");
    }

    /**
     * Describe: 删除角色
     * Param: id
     * Return: ResuBean
     * */
    @DeleteMapping("delete/{id}")
    public Result remove(@PathVariable("id") Integer id){
        roleService.remove(id);
        return new Result(true, StatusCode.SUCCESS,"删除成功");
    }
}

