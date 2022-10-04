package erabbit.service.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.seckill.pojo.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface SkuMapper extends BaseMapper<Sku> {

//    //扣减库存并增加销量
//    @Update("update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where id=#{skuId} and num>=#{num}")
//    int decrCount(OrderItem orderItem);
//
//    //回滚库存(增加库存并扣减销量)
//    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{skuId}")
//    void resumeStockNum(@Param("skuId") String skuId, @Param("num")Integer num);
}
