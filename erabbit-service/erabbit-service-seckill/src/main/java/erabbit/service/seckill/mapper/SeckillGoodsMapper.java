package erabbit.service.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 秒杀商品信息表 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
@Mapper
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

}
