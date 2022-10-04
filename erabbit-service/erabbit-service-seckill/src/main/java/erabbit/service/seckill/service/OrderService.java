package erabbit.service.seckill.service;

import com.erabbit.seckill.pojo.NewOrder;
import com.erabbit.seckill.pojo.dto.Merge;

import java.util.Map;

public interface OrderService {
    Map<String,Object> getCart(String username);

    NewOrder addOrder(Merge merge, String username);
}
