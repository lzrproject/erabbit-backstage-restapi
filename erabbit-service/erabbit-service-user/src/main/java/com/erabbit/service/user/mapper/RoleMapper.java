package com.erabbit.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.common.entity.web.PageDomain;
import com.erabbit.user.pojo.Power;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.dto.PageSelect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findPage(PageSelect pageSelect);

    List<Power> getRolePower(Integer rid);

    @Delete("delete from tb_role_power where role_id = #{rid}")
    Integer delRoleAndPower(Integer rid);

    Integer batchAddRoleAndPower(Map<String,Object> map);
}
