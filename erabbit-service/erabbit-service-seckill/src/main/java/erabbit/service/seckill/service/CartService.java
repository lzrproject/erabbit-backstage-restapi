package erabbit.service.seckill.service;

import com.erabbit.seckill.pojo.Order;
import com.erabbit.seckill.pojo.OrderItem;
import com.erabbit.seckill.pojo.dto.Merge;

import java.util.List;
import java.util.Map;

public interface CartService {
    //合并购物车
    void addCart(List<Merge> cartList, String username);

    void addCartOne(Merge merge,String username);

    //查询购物车数据
    List<Order> list(String username);

    void delCart(String[] skuId,String username);
}
