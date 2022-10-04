package erabbit.service.seckill.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erabbit.common.entity.DateUtil;
import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import erabbit.service.seckill.mapper.SeckillGoodsMapper;
import erabbit.service.seckill.service.GoodsService;
import erabbit.service.seckill.service.SeckillGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.erabbit.common.entity.DateUtil.addDateHour;
import static com.erabbit.common.entity.DateUtil.toDayStartHour;

@Component
@Slf4j
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GoodsService goodsService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis(){
        //获取时间段集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        //循环时间段
        for (Date startTime : dateMenus) {
            // namespace = SeckillGoods_20195712
            String extName = DateUtil.date2Str(startTime);

            Date endTime = addDateHour(startTime, 2);

            QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status","1")
                    .gt("stock_count",0)
                    .ge("start_time",startTime)
                    .le("end_time",endTime);
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + extName).keys();
            if(keys!=null && keys.size()>0){
                queryWrapper.notIn("id",keys);
            }
            List<SeckillGoods> list = seckillGoodsMapper.selectList(queryWrapper);
            //将秒杀商品数据存入到Redis缓存
            for (SeckillGoods seckillGood : list) {
                log.info("SeckillGoods_{}",extName);
                GoodsDetail detail = goodsService.getOne(seckillGood.getId());
                redisTemplate.boundHashOps("SeckillGoods_"+extName).put(seckillGood.getId(),detail);
                redisTemplate.expire("SeckillGoods_"+extName,startTime.getHours()+2, TimeUnit.HOURS);

                //商品数据队列存储,防止高并发超卖
                Long[] ids = pushIds(seckillGood.getStockCount(), seckillGood.getId());
                redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGood.getId()).leftPushAll(ids);
                //自增计数器
                redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGood.getId(),seckillGood.getStockCount());

            }
        }
    }
    /***
     * 将商品ID存入到数组中
     * @param len:长度
     * @param id :值
     * @return
     */
    public Long[] pushIds(int len,Long id){
        Long[] ids = new Long[len];
        for (int i = 0; i <ids.length ; i++) {
            ids[i]=id;
        }
        return ids;
    }

    public static void main(String[] args) {
        System.out.println(new Date());
    }
}