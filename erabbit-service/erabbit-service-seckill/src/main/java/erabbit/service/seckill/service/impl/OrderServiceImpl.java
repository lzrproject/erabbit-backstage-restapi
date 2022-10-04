package erabbit.service.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.IdWorker;
import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.NewOrder;
import com.erabbit.seckill.pojo.Order;
import com.erabbit.seckill.pojo.OrderItem;
import com.erabbit.seckill.pojo.dto.Merge;
import com.erabbit.seckill.pojo.vo.Summary;
import com.erabbit.user.pojo.Address;
import erabbit.service.seckill.mapper.OrderMapper;
import erabbit.service.seckill.mapper.SeckillOrderMapper;
import erabbit.service.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public Map<String,Object> getCart(String username) {
        List<Order> orderList = redisTemplate.boundHashOps("Cart_"+username).values();

//        String skuId = goodsDetail.getId();
//        List<Object> specs = goodsDetail.getSpecs();
//        JSONObject spec = JSONObject.parseObject(JSON.toJSONString(specs.get(0)));
//        String text = spec.getString("name") + ":" + spec.getString("values");

        List<Order> list = new ArrayList<>();

        Integer summaryPayPrice = 0;
        Integer summaryPrice = 0;
        for (Order orderCart : orderList) {
            Integer count = orderCart.getCount();
            String payPriceStr = orderCart.getPayPrice();
            String priceStr = orderCart.getPrice();
            int payPrice = Integer.valueOf(payPriceStr) * count;
            int price = Integer.valueOf(priceStr) * count;
            summaryPayPrice += payPrice;
            summaryPrice += price;

            Order order = new Order();
            order.setAttrsText(orderCart.getAttrsText());
            order.setCount(count);
//        order.setDiscount();
            order.setId(orderCart.getId());
            order.setIsCollect(orderCart.getIsCollect());
            order.setIsEffective(orderCart.getIsEffective());
            order.setName(orderCart.getName());
            order.setPayPrice(payPriceStr);
            order.setPrice(priceStr);
            order.setTotalPayPrice(String.valueOf(payPrice));
            order.setTotalPrice(String.valueOf(price));
            order.setPicture(orderCart.getPicture());
            order.setSelected(orderCart.isSelected());
            order.setSkuId(orderCart.getSkuId());
            order.setStock(orderCart.getStock());
            list.add(order);
        }
        Summary summary = new Summary();
        summary.setPostFee(0);
        summary.setDiscountPrice(0);
        summary.setGoodsCount(1);
        summary.setTotalPrice(summaryPrice.toString());
        summary.setTotalPayPrice(summaryPayPrice.toString());
        List<Address> addresses = seckillOrderMapper.getAddressList(username);
//
        Map<String,Object> map = new HashMap<>();
        map.put("goods", list);
        map.put("summary",summary);
        map.put("userAddresses",addresses);
        return map;
    }

    @Override
    @Transactional
    public NewOrder addOrder(Merge merge,String username) {
        List<Order> values = redisTemplate.boundHashOps("Cart_" + username).values();

        String orderId = String.valueOf(idWorker.nextId());
        NewOrder.NewOrderBuilder builder = NewOrder.builder()
                .id(orderId)
                .addressId(merge.getAddressId())
                .orderState(1)
                .createTime(LocalDateTime.now())
                .payLatestTime(LocalDateTime.now().plusDays(2))
                .postFee(0);


        Integer totalNum = 0;
        Integer totalMoney = 0;
        Integer payMoney = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (Order value:values) {
            Integer count = value.getCount();
            int price = Integer.parseInt(value.getPrice());

            totalNum += count;
            totalMoney += Integer.parseInt(value.getTotalPrice());
            payMoney += Integer.parseInt(value.getTotalPayPrice());

            OrderItem orderItem = new OrderItem();
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setSpuId(value.getId());
            orderItem.setSkuId(value.getSkuId());
            orderItem.setOrderId(orderId);
            orderItem.setName(value.getName());
            orderItem.setPrice(price);
            orderItem.setNum(count);
            orderItem.setMoney(price*count);
            orderItem.setPayMoney(Integer.parseInt(value.getPayPrice())*count);
            orderItem.setImage(value.getPicture());
//            orderItem.setWeight();
            orderItem.setPostFee(0);
            orderItem.setAttrsText(value.getAttrsText());
            orderItems.add(orderItem);

        }
        NewOrder newOrder = builder.totalNum(totalNum)
                .totalMoney(totalMoney)
                .payMoney(payMoney).build();

        orderMapper.insertOrderItem(orderItems);
        orderMapper.insert(newOrder);

        redisTemplate.delete("Cart_" + username);
        return newOrder;
    }

    public static void main(String[] args) {
//        System.out.println(LocalDateTime.now().plusDays(2));
    }
}
