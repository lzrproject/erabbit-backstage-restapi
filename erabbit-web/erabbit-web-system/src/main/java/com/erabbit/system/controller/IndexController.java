package com.erabbit.system.controller;

import com.erabbit.common.entity.CookieUtil;
import com.erabbit.common.entity.Result;
import com.erabbit.user.feign.PowerFeign;
import com.erabbit.user.pojo.Menu;
import com.erabbit.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/***
 * 主页跳转
 */
@Controller
@RequestMapping
//@CrossOrigin //跨域
public class IndexController {

    @Autowired
    private PowerFeign powerFeign;

    @GetMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response,Model model){
        User user = powerFeign.getUser();
        model.addAttribute("username",user.getUsername());
        model.addAttribute("avatar",user.getAvatar());
        return "index";
    }

    @GetMapping("menu")
    @ResponseBody
    public Object getUserMenu(){

        List<Menu> result = powerFeign.getUserMenu();

        return result;
    }

    /**
     * Describe: 获取主页视图
     * Param: ModelAndView
     * Return: 主页视图
     * */
    @GetMapping("/console")
    public ModelAndView home(Model model)
    {
        return new Result().jumpPage("console/console");
    }

    /**
     * 查询消息
     *
     * @return*/
    @ResponseBody
    @GetMapping("system/notice/notice")
    public boolean notice(){

        return false;
    }


}
