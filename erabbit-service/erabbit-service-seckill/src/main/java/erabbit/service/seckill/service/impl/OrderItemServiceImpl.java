package erabbit.service.seckill.service.impl;

import com.erabbit.seckill.pojo.OrderItem;
import erabbit.service.seckill.mapper.OrderItemMapper;
import erabbit.service.seckill.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单对应商品表 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-03-05
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
