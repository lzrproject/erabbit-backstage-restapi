package com.erabbit.user.feign;

import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "service-user")
@RequestMapping(value = "/role")
public interface RoleFeign {
    /**
     * 角色分页列表
     * 角色查询列表
     */
    @PostMapping("/page")
    Result<PageInfo> findPage(@RequestBody PageSelect pageSelect);

    /**
     * 角色添加
     * @param role
     * @return
     */
    @PostMapping("/add")
    Result addAll(@RequestBody Role role);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @GetMapping("/byId")
    Result<Role> findById(@RequestParam("id") Integer id);

    /**
     * 修改角色数据
     * @param role
     * @return
     */
    @PutMapping("/updated")
    Result update(@RequestBody Role role);

    /**
     * Describe: 查询角色权限信息
     * Param: id
     * Return: 返回角色信息
     * */
    @GetMapping("rolePower")
    List<Power> getRolePower(@RequestParam("id") Integer id);

    /**
     * Describe: 保存角色权限数据
     * Param: roleId powerIds
     * Return: 执行结果
     * */
    @PutMapping("upRolePower")
    Result saveRolePower(@RequestParam("roleId") Integer roleId,@RequestParam("powerIds") String powerIds);

    /**
     * Describe: 删除角色
     * Param: id
     * Return: ResuBean
     * */
    @DeleteMapping("delete/{id}")
    Result remove(@PathVariable("id") Integer id);

    /**
     * Describe: 用户批量删除接口
     * Param: ids
     * Return: ResuBean
     * */
    @DeleteMapping("batchDelete")
    Result batchRemove(@RequestBody List<Integer> ids);

    /**
     * 查询所有
     * @return
     */
    @GetMapping("findAll")
    Result<List<Role>> findAll();
}
