package com.erabbit.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.user.feign.PowerFeign;
import com.erabbit.user.pojo.Power;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 *权限管理
 */
@RestController
@RequestMapping(value = "/power")
public class PowerController {
    /**
     * Describe: 基础路径
     * */
    private static String MODULE_PATH = "system/power/";

    @Autowired
    private PowerFeign powerFeign;


    /**
     * Describe: 获取权限列表视图
     * Param ModelAndView
     * Return 权限列表视图
     * */
    @GetMapping(value = "main")
//    @PreAuthorize("hasAuthority('sys:power:main')")
    public ModelAndView index(ModelAndView modelAndView) {

        return new Result().jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取权限列表数据
     * Param ModelAndView
     * Return 权限列表数据
     * */
    @GetMapping("data")
//    @PreAuthorize("hasAuthority('sys:power:data')")
    public Result data(Power power){
        return powerFeign.data(power);
    }

    /**
     * Describe: 获取权限新增视图
     * Param ModelAndView
     * Return 权限新增视图
     * */
    @GetMapping("add")
//    @PreAuthorize("hasAuthority('sys:power:add')")
    public ModelAndView add(){
        return new Result().jumpPage(MODULE_PATH + "add");
    }

    /**
     * Describe: 保存权限信息
     * Param: SysPower
     * Return: ResuBean
     * */
    @PostMapping("save")
//    @PreAuthorize("hasAuthority('sys:power:add')")
    public Result save(@RequestBody Power power){
        if(power.getParentId() == null){
            return new Result(false, StatusCode.ERROR_PARAMS,"请选择上级菜单");
        }
        Result result = powerFeign.addAll(power);
//        power.setId(SequenceUtil.makeStringId());
//        boolean result = sysPowerService.save(sysPower);

        return result;
    }


    /**
     * Describe: 获取父级权限选择数据
     * Param sysPower
     * Return ResuTree
     * */
    @GetMapping("selectParent")
//    @PreAuthorize("hasAuthority('sys:power:add')")
    public JSONObject selectParent(Power power){
        Result<List<Power>> data = powerFeign.data(power);
        Power basePower = new Power();
        basePower.setPowerName("顶级权限");
        basePower.setId(0);
        basePower.setParentId("-1");
        data.getData().add(basePower);
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        jsonObject1.put("code",200);
        jsonObject1.put("message","默认");

        jsonObject.put("status",jsonObject1);
        jsonObject.put("data",data.getData());
        return jsonObject;
    }

    /**
     * Describe: 获取权限修改视图
     * Param ModelAndView
     * Return 权限修改视图
     * */
    @GetMapping("edit")
//    @PreAuthorize("hasAuthority('sys:power:edit')")
    public ModelAndView edit(@RequestParam Integer id,Model model){
//        System.out.println(id);
        model.addAttribute("sysPower",powerFeign.findById(id).getData());
        return new Result().jumpPage(MODULE_PATH + "edit");
    }

    /**
     * Describe: 修改权限信息
     * Param SysPower
     * Return 执行结果
     * */
    @PutMapping("update")
//    @PreAuthorize("hasAuthority('sys:power:edit')")
    public Result update(@RequestBody Power power){
        if(power.getParentId() == null){
            return new Result(false,StatusCode.ERROR_PARAMS,"请选择上级菜单");
        }
        Result result = powerFeign.update(power);
        return result;
    }

    /**
     * Describe: 根据 id 进行删除
     * Param id
     * Return ResuTree
     * */
    @DeleteMapping("remove/{id}")
//    @PreAuthorize("hasAuthority('sys:power:remove')")
    public Result remove(@PathVariable String id){
        Power power = new Power();
        power.setParentId(id);
        Result<List<Power>> result = powerFeign.data(power);
        if(result.getData().size() != 0){
            return new Result(false,StatusCode.ERROR_PARAMS,"请先删除子集");
        }

        Result result1 = powerFeign.remove(id);
        return result1;
    }


    /**
     * Describe: 根据 Id 开启用户
     * enable: 1 为开启  2为禁用
     * Param powerId
     * Return
     * */
    @PutMapping("enable")
    public Result enable(@RequestBody Power power){
        power.setEnable(1);
        Result result = powerFeign.update(power);
        return result;
    }

    /**
     * Describe: 根据 Id 禁用用户
     * enable: 1 为开启  2为禁用
     * Param powerId
     * Return
     * */
    @PutMapping("disable")
    public Result disable(@RequestBody Power power){
        power.setEnable(0);
        Result result = powerFeign.update(power);
        return result;
    }
}
