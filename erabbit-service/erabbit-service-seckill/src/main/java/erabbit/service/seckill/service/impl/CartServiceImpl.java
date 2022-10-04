package erabbit.service.seckill.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.erabbit.seckill.pojo.*;
import com.erabbit.seckill.pojo.dto.Merge;
import erabbit.service.seckill.mapper.SeckillGoodsMapper;
import erabbit.service.seckill.mapper.SkuMapper;
import erabbit.service.seckill.mapper.SpuMapper;
import erabbit.service.seckill.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART_NAME = "Cart_";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    public Order getRedisCart(String skuId,Integer num, String username){
        //1.查询redis中相对应的商品信息
        Order order = (Order) redisTemplate.boundHashOps(CART_NAME + username).get(skuId);
        if (order != null) {
            //2.如果当前商品在redis中的存在,则更新商品的数量与价钱
            order.setCount(order.getCount() + num);
            if (order.getCount() <= 0) {
                //删除该商品
                redisTemplate.boundHashOps(CART_NAME + username).delete(skuId);
                return null;
            }
        } else {
            //3.如果当前商品在redis中不存在,将商品添加到redis中
            Sku sku = skuMapper.selectById(skuId);
            Spu spu = spuMapper.getOne(Long.valueOf(sku.getSpuId()));


            //封装order
            order = this.sku2Order(sku, spu, num);
        }
        return order;
    }

    @Override
    public void addCart(List<Merge> cartList, String username) {
        Map<String, Order> map = new HashMap<>();
        for (Merge cart : cartList) {
            String skuId = cart.getSkuId();
            Integer num = cart.getCount();

            Order redisCart = getRedisCart(skuId, num, username);
            if (redisCart == null) break;
            map.put(skuId,redisCart);
        }
        //3.将orderitem添加到redis中
        redisTemplate.boundHashOps(CART_NAME+username).putAll(map);
    }

    @Override
    public void addCartOne(Merge merge, String username) {
        String skuId = merge.getSkuId();
        Integer num = merge.getCount();
        Order redisCart = getRedisCart(skuId, num, username);
        if (redisCart == null) return;
        redisTemplate.boundHashOps(CART_NAME+username).put(skuId,redisCart);
    }

    //查询购物车列表数据
    @Override
    public List<Order> list(String username) {

        List<Order> orderList = redisTemplate.boundHashOps(CART_NAME + username).values();

        return orderList;
    }

    @Override
    public void delCart(String[] skuId,String username) {
        redisTemplate.boundHashOps(CART_NAME+username).delete(skuId);
    }


    private Order sku2Order(Sku sku, Spu spu, Integer num) {
        List<Spec> objects = JSONArray.parseArray(sku.getSpec(), Spec.class);
        String text = objects.get(0).getName() + ":" + objects.get(0).getValueName();

        Integer price = sku.getPrice();
        String totalPrice = String.valueOf(num * price);

        Order order = new Order();
        order.setAttrsText(text);
        order.setCount(num);
//        order.setDiscount();
        order.setId(spu.getId());
        order.setIsCollect(false);
        order.setIsEffective(true);
        order.setName(spu.getName());
        order.setPrice(price.toString());
        order.setPayPrice(price.toString());
        order.setTotalPrice(totalPrice);
        order.setTotalPayPrice(totalPrice);
        order.setPicture(spu.getImage());
        order.setSelected(true);
        order.setSkuId(sku.getId());
        order.setStock(sku.getNum());


//        OrderItem orderItem = new OrderItem();
//        orderItem.setSpuId(sku.getSpuId());
//        orderItem.setSkuId(sku.getId());
////        orderItem.setName(sku.getName());
//        orderItem.setName(spu.getName());
//        orderItem.setAttrsText(sku.getName());
//        orderItem.setPrice(sku.getPrice());
//        orderItem.setNum(num);
//        orderItem.setMoney(orderItem.getPrice()*num);
//        orderItem.setPayMoney(orderItem.getPrice()*num);
//        orderItem.setImage(sku.getImage());
//        orderItem.setWeight(sku.getWeight()*num);
        //分类信息
//        orderItem.setCategoryId1(spu.getCategory1Id());
//        orderItem.setCategoryId2(spu.getCategory2Id());
//        orderItem.setCategoryId3(spu.getCategory3Id());
        return order;
    }
}
