package erabbit.service.seckill.service;

import com.erabbit.common.entity.SeckillStatus;
import com.erabbit.seckill.pojo.NewOrder;
import com.erabbit.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erabbit.seckill.pojo.dto.Merge;
import com.erabbit.seckill.pojo.vo.TOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
public interface SeckillOrderService extends IService<SeckillOrder> {
    /***
     * 添加秒杀订单
     * @param id:商品ID
     * @param time:商品秒杀开始时间
     * @param username:用户登录名
     * @return
     */
    String add(Long id, String time, String username);

    Map<String,Object> orderInfo(String time, Long id, String username);

    TOrder getOrderList(Merge merge,String username);

    NewOrder getOrderOne(String orderId, String username);
    void cancelOrder(String orderId,String cancelReason,String username);

    /***
     * 抢单状态查询
     * @param username
     */
    List<SeckillStatus> queryStatus(String username);
}
