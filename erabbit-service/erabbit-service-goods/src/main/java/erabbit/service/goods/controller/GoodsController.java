package erabbit.service.goods.controller;

import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import erabbit.service.goods.service.GoodsService;
import erabbit.service.goods.service.SeckillGoodsService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("goods")
@RestController
@CrossOrigin
public class GoodsController {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private GoodsService goodsService;

    /****
     * URL:/seckill/goods/list
     * 对应时间段秒杀商品集合查询
     * 调用Service查询数据
     * @param
     */
    @GetMapping(value = "/list/{time}")
    public List<GoodsDetail> list(@PathVariable("time") String time){
        //调用Service查询数据
        return seckillGoodsService.list(time);
    }

    /**
     * 根据spuId获取商品详情
     */
    @GetMapping("getOne")
    public GoodsDetail getOne(Long spuId){
        return goodsService.getOne(spuId);
    }
}
