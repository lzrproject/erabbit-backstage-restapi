package com.erabbit.user.feign;

import com.erabbit.user.pojo.vo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-user")
@RequestMapping("webUser")
public interface WebUserFeign {
    @GetMapping("getUserInfo")
    public UserInfo getUserInfo();
}
