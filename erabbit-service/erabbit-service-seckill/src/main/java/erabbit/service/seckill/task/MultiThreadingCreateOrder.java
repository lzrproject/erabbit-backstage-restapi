package erabbit.service.seckill.task;

import com.erabbit.common.entity.IdWorker;
import com.erabbit.common.entity.SeckillStatus;
import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import com.erabbit.seckill.pojo.SeckillOrder;
import erabbit.service.seckill.mapper.SeckillGoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    /***
     * 多线程下单操作
     */
    @Async
    public String createOrder(){
        //从队列中获取排队信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();

        if (seckillStatus == null) return null;
        try {
            //从队列中获取一个商品
            Object sgood = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).rightPop();
            if(sgood==null){
                //清理当前用户的排队信息
                clearQueue(seckillStatus);
                log.info("{} 商品抢完了！！！",seckillStatus.getUsername());
                return null;
            }


            //时间区间
            String time = seckillStatus.getTime();
            //用户登录名
            String username=seckillStatus.getUsername();
            //用户抢购商品
            Long id = seckillStatus.getGoodsId();

            //获取商品数据
            GoodsDetail goods = (GoodsDetail) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);

//            //如果没有库存，则直接抛出异常
//            if(goods==null || goods.getInventory()<=0){
//                throw new RuntimeException("已售罄!");
//            }
            //如果有库存，则创建秒杀商品订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSeckillId(id);
            seckillOrder.setMoney(goods.getPrice());
            seckillOrder.setUserId(username);
            seckillOrder.setCreateTime(LocalDateTime.now());
            seckillOrder.setStatus("0");

            //将秒杀订单存入到Redis中
            redisTemplate.boundHashOps("SeckillOrder_"+username).put(id,seckillOrder);

            //商品库存-1
            Long surplusCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(id, -1);//商品数量递减

            //库存减少
            goods.setInventory(surplusCount.intValue());
//            goods.setSalesCount(goods.getSalesCount()+1);

            //判断当前商品是否还有库存
            if(surplusCount<=0){
                SeckillGoods seckillGoods = new SeckillGoods();
                seckillGoods.setId(id);
                seckillGoods.setStockCount(0);
                //并且将商品数据同步到MySQL中
                seckillGoodsMapper.updateById(seckillGoods);
                //如果没有库存,则清空Redis缓存中该商品
//                redisTemplate.boundHashOps("SeckillGoods_" + time).delete(id);
            }else{
                //如果有库存，则直数据重置到Reids中
                redisTemplate.boundHashOps("SeckillGoods_" + time).put(id,goods);
            }

            //抢单成功，更新抢单状态,排队->等待支付
            seckillStatus.setStatus(2);
            seckillStatus.setOrderId(seckillOrder.getId());
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
            redisTemplate.boundHashOps("UserQueueStatus_"+username).put(id,seckillStatus);
            return seckillOrder.getId().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 清理用户排队信息
     * @param seckillStatus
     */
    public void clearQueue(SeckillStatus seckillStatus){
        String queueKey = seckillStatus.getUsername() + "_" + seckillStatus.getGoodsId();
        //清理排队标示
        redisTemplate.boundHashOps("UserQueueCount").delete(queueKey);

        //清理抢单标示
        redisTemplate.boundHashOps("UserQueueStatus").delete(queueKey);
    }
}
