package com.erabbit.service.user.service.impl;

import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.service.user.mapper.UserMapper;
import com.erabbit.service.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.UserRole;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUsername(String username) {
        return userMapper.findUsername(username);
    }

    @Override
    public User findUsernameDetails(String username) {
        User user = userMapper.findUsernameDetails(username);
        String[] split = user.getRoleIds().split(",");
        String powerCodeString = userMapper.findPowerCodeString(split);
        user.setPowerCode(powerCodeString);

        return user;
    }

    @Override
    public PageInfo<User> findPage(PageSelect pageSelect) {
        PageHelper.startPage(pageSelect.getPage(),pageSelect.getLimit());
        List<User> userList = userMapper.findPage(pageSelect);

        return new PageInfo<User>(userList);
    }

    @Override
    public Integer addRoleAndUser(UserRole userRole) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uid",userRole.getUserId());
        map.put("rids",userRole.getRoleIds().split(","));
        Integer roleAndUser = userMapper.batchAddRoleAndUser(map);
        return roleAndUser;
    }

    @Override
    public List<Role> findRoleByUid(Integer uid) {
        return userMapper.findRoleByUid(uid);
    }

    @Override
    @Transactional
    public Integer update(User user) {
        Integer userId = user.getId();
        userMapper.updateById(user);
        userMapper.delRoleByUid(userId);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uid", userId);
        map.put("rids",user.getRoleIds().split(","));
        userMapper.batchAddRoleAndUser(map);
        return userMapper.batchAddRoleAndUser(map);
    }

    @Override
    @Transactional
    public Integer delete(Integer uid) {
        userMapper.delRoleByUid(uid);
        return userMapper.deleteById(uid);
    }
}
