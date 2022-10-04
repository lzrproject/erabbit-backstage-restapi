package com.erabbit.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.UserRole;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
public interface UserService extends IService<User> {
    User findUsername(String username); //根据用户名查询

    User findUsernameDetails(String username);  //查询用户、角色、权限

    PageInfo<User> findPage(PageSelect pageSelect); //查询用户列表

    Integer addRoleAndUser(UserRole userRole);

    List<Role> findRoleByUid(Integer uid);

    Integer update(@RequestBody User user);

    Integer delete(Integer uid);
}
