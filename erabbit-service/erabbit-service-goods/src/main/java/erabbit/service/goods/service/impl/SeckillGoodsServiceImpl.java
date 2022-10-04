package erabbit.service.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erabbit.common.entity.DateUtil;
import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import erabbit.service.goods.mapper.SeckillGoodsMapper;
import erabbit.service.goods.service.SeckillGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.erabbit.common.entity.DateUtil.addDateHour;

/**
 * <p>
 * 秒杀商品信息表 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<GoodsDetail> list(String key) {
        return redisTemplate.boundHashOps("SeckillGoods_"+key).values();
    }

    /****
     * 根据商品ID查询商品详情
     * @param time:时间区间
     * @param id:商品ID
     * @return
     */
//    @Override
//    public GoodsDetail one(String time, Long id) {
//        return (GoodsDetail) redisTemplate.boundHashOps("SeckillGoods_"+time).get(id);
//    }
}
