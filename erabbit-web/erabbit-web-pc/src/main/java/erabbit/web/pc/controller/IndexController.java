package erabbit.web.pc.controller;

import com.erabbit.user.feign.WebUserFeign;
import com.erabbit.user.pojo.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private WebUserFeign webUserFeign;

    @GetMapping("accountDetail")
    public UserInfo accountDetail(){
        return webUserFeign.getUserInfo();
    }
}
