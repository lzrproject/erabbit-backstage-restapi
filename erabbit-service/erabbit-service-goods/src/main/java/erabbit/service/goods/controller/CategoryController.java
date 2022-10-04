package erabbit.service.goods.controller;


import com.alibaba.fastjson.JSONObject;
import com.erabbit.seckill.pojo.Category;
import erabbit.service.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品类目 前端控制器
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-02-20
 */
@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("head")
    public List<Category> findAllCategory(){
        return categoryService.getCategoryList();
    }

    public static void main(String[] args) {
        String str = "[{\"a\":{\"aTest\":\"a\",\"bTest\":1}}]";
        List<Map> maps = JSONObject.parseArray(str, Map.class);

    }
}

