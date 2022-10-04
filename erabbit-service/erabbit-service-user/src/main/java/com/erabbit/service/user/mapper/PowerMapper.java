package com.erabbit.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.user.pojo.Menu;
import com.erabbit.user.pojo.Power;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Mapper
public interface PowerMapper extends BaseMapper<Power> {
    List<Power> findAll(Power power);

    @Delete("delete from tb_role_power where power_id =#{id}")
    Integer deleteRoleAndPower(String id);

    List<Menu> findMenuAll(Integer uid);
}
