package erabbit.service.seckill.service;

import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 秒杀商品信息表 服务类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
public interface SeckillGoodsService extends IService<SeckillGoods> {
    /***
     * 获取指定时间对应的秒杀商品列表
     * @param key
     */
    List<GoodsDetail> list(String key);

    /****
     * 根据ID查询商品详情
     * @param time:时间区间
     * @param id:商品ID
     */
    GoodsDetail one(String time,Long id);
}
