package com.erabbit.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.dto.PageSelect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from tb_user where username = #{username}")
    User findUsername(String username);

    User findUsernameDetails(String username);  //查询用户基本信息、角色、权限
    String findPowerCodeString(String[] rids);

    List<User> findPage(PageSelect pageSelect); //查询用户列表

    Integer batchAddRoleAndUser(Map<String,Object> maps);    //添加用户角色关联id

    List<Role> findRoleByUid(Integer uid);

    @Delete("DELETE FROM tb_user_role WHERE user_id = #{uid}")
    Integer delRoleByUid(Integer uid);

}
