package com.erabbit.seckill.feign;

import com.erabbit.seckill.pojo.Order;
import com.erabbit.seckill.pojo.OrderItem;
import com.erabbit.seckill.pojo.dto.Merge;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "seckill")
@RequestMapping("/cart")
public interface CartFeign {

    /**
     * 合并购物车
     * @Param
     */
    @PostMapping("/merge")
    public void merge(@RequestBody List<Merge> cartList);

    @PostMapping("/addCart")
    public void addCart(@RequestBody Merge merge);

    /**
     * 获取购物车
     * @return
     */
    @GetMapping("/list")
    public List<Order> list();

    @DeleteMapping("delete")
    public void delCart(@RequestBody Merge merge);
}
