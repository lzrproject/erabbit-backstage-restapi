package erabbit.service.seckill.mapper;

import com.erabbit.seckill.pojo.NewOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.seckill.pojo.OrderItem;
import com.erabbit.seckill.pojo.TSku;
import com.erabbit.seckill.pojo.vo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-03-05
 */
@Mapper
public interface OrderMapper extends BaseMapper<NewOrder> {
    Integer insertOrderItem(List<OrderItem> orderItems);

    @Update("update tb_order set order_state = #{orderState} where id = #{orderId} ")
    Integer updateOrderState(@Param("orderId") String orderId ,@Param("orderState") Integer orderState);

    @Select("select id,pay_latest_time,order_state,pay_channel,pay_money,pay_type,post_fee,total_money,total_num,create_time,update_time from tb_order")
    List<Item> getOrderList();

    List<TSku> getOrderItem(String orderId);

    NewOrder getOrder(String orderId);
}
