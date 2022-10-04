package com.erabbit.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
public interface RoleService extends IService<Role> {
    //分页+条件查询
    PageInfo<Role> findPage(PageSelect pageSelect);

    List<Power> getRolePower(Integer id);

    Integer saveRolePower(Integer rid,String powerIds);

    Integer remove(Integer id);

}
