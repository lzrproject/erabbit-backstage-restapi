package com.erabbit.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erabbit.user.pojo.Menu;
import com.erabbit.user.pojo.Power;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
public interface PowerService extends IService<Power> {
    List<Power> findAll(Power power);

    Integer update(Power power);

    Integer remove(String id);

    List<Menu> getUserMenu(Integer uid);
}
