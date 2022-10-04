package com.erabbit.seckill.feign;

import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "goods")
@RequestMapping("/goods")
public interface GoodsFeign {
    @GetMapping(value = "/list/{time}")
    public List<GoodsDetail> list(@PathVariable("time") String time);
}
