package com.erabbit.seckill.feign;

import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "seckill")
@RequestMapping("/seckillGoods")
public interface SecKillGoodsFeign {
    /****
     * URL:/seckill/goods/one
     * 根据ID查询商品详情
     * 调用Service查询商品详情
     * @param time
     * @param id
     */
//    @GetMapping(value = "/one/{time}/{id}")
//    public GoodsDetail one(@PathVariable("time") String time,@PathVariable("id") Long id);
}
