package com.erabbit.service.user.service.impl;

import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.service.user.mapper.RoleMapper;
import com.erabbit.service.user.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.Role;
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
 * 角色表 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> findPage(PageSelect pageSelect) {

        PageHelper.startPage(pageSelect.getPage(),pageSelect.getLimit());
        List<Role> roleList = roleMapper.findPage(pageSelect);

        return new PageInfo<>(roleList);
    }

    @Override
    public List<Power> getRolePower(Integer id) {
        return roleMapper.getRolePower(id);
    }

    @Override
    @Transactional
    public Integer saveRolePower(Integer rid, String powerIds) {
        Map<String,Object> map = new HashMap<>();
        map.put("rid",rid);
        map.put("powerIds",powerIds.split(","));

        roleMapper.delRoleAndPower(rid);

        return roleMapper.batchAddRoleAndPower(map);
    }

    @Override
    @Transactional
    public Integer remove(Integer id) {
        roleMapper.delRoleAndPower(id);
        roleMapper.deleteById(id);
        return null;
    }
}
