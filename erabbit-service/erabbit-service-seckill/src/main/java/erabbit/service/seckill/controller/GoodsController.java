package erabbit.service.seckill.controller;

import com.erabbit.seckill.pojo.GoodsDetail;
import erabbit.service.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("getOne/{id}")
    public GoodsDetail getGoods(@PathVariable("id") Long id){
        return goodsService.getOne(id);
    }
}
