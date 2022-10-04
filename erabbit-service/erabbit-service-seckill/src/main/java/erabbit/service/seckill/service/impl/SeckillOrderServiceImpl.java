package erabbit.service.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.DateUtil;
import com.erabbit.common.entity.IdWorker;
import com.erabbit.common.entity.SeckillStatus;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.seckill.pojo.*;
import com.erabbit.seckill.pojo.dto.Merge;
import com.erabbit.seckill.pojo.vo.Item;
import com.erabbit.seckill.pojo.vo.Summary;
import com.erabbit.seckill.pojo.vo.TOrder;
import com.erabbit.user.pojo.Address;
import erabbit.service.seckill.mapper.OrderMapper;
import erabbit.service.seckill.mapper.SeckillGoodsMapper;
import erabbit.service.seckill.mapper.SeckillOrderMapper;
import erabbit.service.seckill.service.SeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import erabbit.service.seckill.task.MultiThreadingCreateOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.checkerframework.checker.units.qual.A;
import org.mockito.internal.util.collections.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
@Service
@Slf4j
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    /****
     * 添加订单
     * @param id
     * @param time
     * @param username
     */
    @Override
    public String add(Long id, String time, String username){
//        String queueKey = username + "_" + id;

        //递增，判断是否排队  UserQueueCount_username
        Long userQueueCount = redisTemplate.boundHashOps("UserQueueCount_"+username).increment(id, 1);
        if(userQueueCount>1){
            log.info("{} 重复下单",username);
            //100：表示有重复抢单
//            throw new RuntimeException("重复下单");
            return "repeat";
        }

        //排队信息封装
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(),1, id,time);

        //将秒杀抢单信息存入到Redis中,这里采用List方式存储,List本身是一个队列
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);

        //将抢单状态存入到Redis中
        redisTemplate.boundHashOps("UserQueueStatus_"+username).put(id,seckillStatus);

        return multiThreadingCreateOrder.createOrder();

    }

    @Override
    public Map<String,Object> orderInfo(String time, Long id,String username) {
        GoodsDetail goodsDetail = (GoodsDetail) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);
        String skuId = goodsDetail.getId();
        List<Object> specs = goodsDetail.getSpecs();
        JSONObject spec = JSONObject.parseObject(JSON.toJSONString(specs.get(0)));
        String text = spec.getString("name") + ":" + spec.getString("values");


        Order order = new Order();
        order.setAttrsText(text);
        order.setCount(1);
//        order.setDiscount();
        order.setId(skuId);
        order.setIsCollect(false);
        order.setIsEffective(true);
        order.setName(goodsDetail.getName());
        order.setPayPrice(goodsDetail.getPrice());
        order.setPrice(goodsDetail.getOldPrice());
        order.setTotalPayPrice(goodsDetail.getPrice());
        order.setTotalPrice(goodsDetail.getOldPrice());
        order.setPicture(goodsDetail.getSmallPic());
        order.setSelected(true);
        order.setSkuId(skuId);
        order.setStock(goodsDetail.getInventory());

        Summary summary = new Summary();
        summary.setPostFee(0);
        summary.setDiscountPrice(0);
        summary.setGoodsCount(1);
        summary.setTotalPrice(goodsDetail.getOldPrice());
        summary.setTotalPayPrice(goodsDetail.getPrice());
        List<Address> addresses = seckillOrderMapper.getAddressList(username);

        Map<String,Object> map = new HashMap<>();
        map.put("goods",Arrays.asList(order));
        map.put("summary",summary);
        map.put("userAddresses",addresses);
        return map;
    }

    @Override
    public TOrder getOrderList(Merge merge,String username) {
        Integer pageSize = merge.getPageSize();
        Integer pageNum = merge.getPageNum();
//        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder_"+username).get(username);
        BoundHashOperations hashOps = redisTemplate.boundHashOps("SeckillOrder_" + username);
        List values = hashOps.values();


        TOrder tOrder = new TOrder();
        tOrder.setPage(pageNum);
        tOrder.setPageSize(pageSize);


        //普通订单
        List<Item> items = orderMapper.getOrderList();
        int size = values.size()+items.size();
        for (Item item:items) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parse2 = format.parse(item.getPayLatestTime());
                Date now = new Date();
                boolean after = parse2.after(now);
                if (after){
                    long timeUnit = parse2.getTime() - now.getTime();
                    Long a = timeUnit/1000;
                    item.setCountdown(a);
                }else {
                    if (item.getOrderState() == 1){
                        orderMapper.updateOrderState(item.getId(),6);
                    }
                    item.setOrderState(6);
                    item.setCountdown(0L);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            item.getCreateTime()
//            item.setCountdown();
            List<TSku> skus = orderMapper.getOrderItem(item.getId());
            item.setSkus(skus);
        }

        //秒杀订单
//        List<Item> items = new ArrayList<>();

        tOrder.setCounts(size);
        tOrder.setPages((int) Math.ceil(size/pageSize));
        for (int i = 0; i < values.size(); i++) {
            SeckillOrder seckillOrder = (SeckillOrder) values.get(i);
            Item item = orderItem(seckillOrder);
            items.add(item);
        }

        items = items.stream()
                .sorted(Comparator.comparing(Item::getCreateTime).reversed())
                .collect(Collectors.toList());
        List list = startPage(items, pageNum, pageSize);

//        SeckillStatus queueStatus = (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
        tOrder.setItems(list);
        return tOrder;
    }

    @Override
    public NewOrder getOrderOne(String orderId,String username) {
        List<SeckillOrder> values = redisTemplate.boundHashOps("SeckillOrder_" + username).values();
        List<Address> addresses = seckillOrderMapper.getAddressList(username);
        NewOrder.NewOrderBuilder builder = NewOrder.builder();
        assert values != null;
        List<SeckillOrder> seckillOrders = values.stream().filter(map -> {
            Long orderIds = map.getId();
            Long seckillId = map.getSeckillId();
            if (orderId.equals(String.valueOf(orderIds))) {
                int money = new BigDecimal(map.getMoney()).intValue();
                TSku tSku = seckillOrderMapper.getSku(seckillId);
                List<TSku> list = new ArrayList<>();
                list.add(tSku);
                builder.id(String.valueOf(seckillId))
                        .orderState("0".equals(map.getStatus()) ? 1 : 2)
//                        .payChannel()
//                        .payLatestTime()
                        .payMoney(money)
//                        .payType(map.get)
                        .createTime(map.getCreateTime())
                        .totalMoney(money)
                        .totalNum(1)
                        .skus(list)
                        .address(addresses.get(0));

                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (seckillOrders.size() != 0) {
            return builder.build();
        }
        NewOrder newOrder = orderMapper.getOrder(orderId);
        newOrder.setAddress(seckillOrderMapper.getAddress(newOrder.getAddressId()));
        return newOrder;
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId, String cancelReason, String username) {

        BoundHashOperations hashOps = redisTemplate.boundHashOps("SeckillOrder_" + username);
        List values = hashOps.values();
        values.stream().map(val -> {
            SeckillOrder seckillOrder = (SeckillOrder) val;
            if (orderId.equals(seckillOrder.getId().toString())) {
                Long seckillId = seckillOrder.getSeckillId();
                seckillOrder.setStatus("6");
                hashOps.put(seckillId,seckillOrder);

                redisTemplate.boundHashOps("UserQueueCount_"+username).delete(seckillId);
            }
            return null;
        }).collect(Collectors.toList());
//        if (seckillOrder != null) {
//            Long seckillId = seckillOrder.getSeckillId();
//            seckillOrder.setStatus("6");

//        }

        orderMapper.updateOrderState(orderId,6);
    }

    public Item orderItem(SeckillOrder seckillOrder){
        LocalDateTime createTime = seckillOrder.getCreateTime();
        Item item = new Item();
//        item.setCountdown(50000L);
        item.setCreateTime(createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        item.setId(seckillOrder.getId().toString());
        //秒杀状态  1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
        String status = seckillOrder.getStatus();
        Integer newStatus = 6;
        if ("0".equals(status))
            newStatus = 1;
        else if ("1".equals(status))
            newStatus = 2;


        item.setCountdown(Duration.between(LocalDateTime.now(),createTime.plusHours(2)).toMillis()/1000);
        item.setOrderState(newStatus);
//        item.setPayChannel();
        item.setPayLatestTime(createTime.minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        item.setPayMoney(seckillOrder.getMoney());
        item.setPayType(1);
        item.setPostFee(0);
        item.setTotalMoney(seckillOrder.getMoney());
        item.setTotalNum(1);

        TSku tSku = seckillOrderMapper.getSku(seckillOrder.getSeckillId());
        String attrsText = tSku.getAttrsText();
        List<Spec> specs = JSONArray.parseArray(attrsText, Spec.class);
        String newAttr = "";
        for (Spec spec:specs){
            newAttr += spec.getName()+":" +spec.getValueName()+"  ";
        }
        tSku.setAttrsText(newAttr);
        tSku.setQuantity(1);

        item.setSkus(Arrays.asList(tSku));
        return item;
    }

    /***
     * 抢单状态查询
     * @param username
     * @return
     */
    @Override
    public List<SeckillStatus> queryStatus(String username) {
//        return (SeckillStatus) redisTemplate.boundHashOps("UserQueueStatus").get(username);
        return redisTemplate.boundHashOps("UserQueueStatus_"+username).values();
    }

    public static List startPage(List list, Integer pageNum,
                                 Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        List pageList = list.subList(fromIndex, toIndex);

        return pageList;
    }

    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastTime = dateTime.plusHours(2);
        System.out.println(Duration.between(now,lastTime).toMillis()/1000);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        String a = "2022-03-06 14:00:00";
//        String b = "2022-03-08 14:00:00";
//        try {
//            Date parse2 = format.parse(b);
//            Date now = new Date();
//            boolean after = parse2.after(now);
//            if (after){
//                long timeUnit = parse2.getTime() - now.getTime();
//                Long a = timeUnit/1000;
//                System.out.println(a);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }


}
