package com.erabbit.user.handle;

import com.erabbit.common.entity.Result;
import com.erabbit.user.feign.UserFeign;
import com.erabbit.user.pojo.Role;
import com.erabbit.user.pojo.User;
import com.erabbit.user.pojo.dto.PageSelect;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@Slf4j
//@RequestMapping("/product")
public class FallbackServiceHandler implements UserFeign {
    @Override
    public Result addAll(User user) {
        return null;
    }

    @Override
    public Result<User> findUsername(String username) {
        log.info("Feign findUsername接口服务降级");
        return null;
    }

    @Override
    public Result<User> findUsernameDetails(String username) {
        return null;
    }

    @Override
    public Result<PageInfo> findPage(PageSelect pageSelect) {
        return null;
    }

    @Override
    public Result<User> findById(Integer id) {
        return null;
    }

    @Override
    public Result<List<Role>> findRoleByUid(Integer uid) {
        return null;
    }

    @Override
    public Result update(User user) {
        return null;
    }

    @Override
    public Result editEnable(User user) {
        return null;
    }

    @Override
    public Result delete(Integer id) {
        return null;
    }
}
