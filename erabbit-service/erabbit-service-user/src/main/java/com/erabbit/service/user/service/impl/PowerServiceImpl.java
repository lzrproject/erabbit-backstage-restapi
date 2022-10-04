package com.erabbit.service.user.service.impl;

import com.erabbit.service.user.mapper.PowerMapper;
import com.erabbit.service.user.service.PowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erabbit.user.pojo.Menu;
import com.erabbit.user.pojo.Power;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2021-12-31
 */
@Service
public class PowerServiceImpl extends ServiceImpl<PowerMapper, Power> implements PowerService {

    @Autowired
    private PowerMapper powerMapper;

    @Override
    public List<Power> findAll(Power power) {
        return powerMapper.findAll(power);
    }

    @Override
    public Integer update(Power power) {
        return powerMapper.updateById(power);
    }

    @Override
    @Transactional
    public Integer remove(String id) {
        powerMapper.deleteById(id);
        powerMapper.deleteRoleAndPower(id);
        return null;
    }

    @Override
    public List<Menu> getUserMenu(Integer uid) {
        List<Menu> menuList = powerMapper.findMenuAll(uid);
        List<Menu> menusNewList = toUserMenu(menuList, 0);

        for (int i = 0;i < menusNewList.size();i++){
            Menu menu = menusNewList.get(i);
            if(menu.getEnable().equals(0)){
                menusNewList.remove(i);
                i--;
            }else {
                for(int j = 0; j < menu.getChildren().size(); j++){
                    if(menu.getChildren().get(j).getEnable().equals(0)) {
                        menu.getChildren().remove(j);
                        j--;
                    }
                }
            }
        }

        return menusNewList;
    }

    //菜单分类
    public List<Menu> toUserMenu(List<Menu> menuList,Integer parentId) {
        List<Menu> list = new ArrayList<>();
        for (Menu menu : menuList) {

            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(toUserMenu(menuList, menu.getId()));
                list.add(menu);
            }

        }
        return list;
    }
}
