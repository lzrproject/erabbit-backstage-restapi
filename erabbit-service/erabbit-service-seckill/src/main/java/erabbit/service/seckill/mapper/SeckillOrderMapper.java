package erabbit.service.seckill.mapper;

import com.erabbit.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.seckill.pojo.TSku;
import com.erabbit.user.pojo.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 秒杀订单表 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
@Mapper
public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {
    @Select("select * from tb_address where username = #{username}")
    List<Address> getAddressList(String username);

    @Select("select * from tb_address where id = #{addressId}")
    Address getAddress(Integer addressId);

    TSku getSku(Long seckillId);
}
