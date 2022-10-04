package erabbit.service.seckill.controller;


import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.SeckillStatus;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.seckill.pojo.dto.Merge;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import erabbit.service.seckill.config.TokenDecode;
import erabbit.service.seckill.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀订单表 前端控制器
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/seckillOrder")
@RestControllerAdvice
public class SeckillOrderController {
    @Autowired
    private SeckillOrderService seckillOrderService;


    /**
     * 生成订单
     * @param time
     * @param seckillId
     * @return
     */
    @GetMapping("orderInfo")
    public Map<String,Object> orderInfo(@RequestParam("time") String time, @RequestParam("seckillId") Long seckillId){
        String username = TokenDecode.getUserInfo().get("username");
//        String username = "admins";
        return seckillOrderService.orderInfo(time,seckillId,username);
    }


    /****
     * 添加订单
     * 调用Service增加订单
     * 匿名访问：anonymousUser
     * @param merge
     */
    @PostMapping(value = "/add")
    @HystrixCommand(fallbackMethod = "addOrderFallbackHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000"),
            @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "100")
    },threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "100"),
            @HystrixProperty(name = "maxQueueSize", value = "300"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "300")
    })
    public Result add(@RequestBody Merge merge){
        //用户登录名
        String username = TokenDecode.getUserInfo().get("username");
//            String username = merge.getUsername();
        String time = merge.getTime();
        Long seckillId = merge.getSeckillId();

        //调用Service增加订单
        String bo = seckillOrderService.add(seckillId, time, username);
        if ("repeat".equals(bo)){
            return new Result(false,StatusCode.ERROR_RESPONSE,"请勿重新下单");
        }
        if(bo != null){
            log.info("{} 抢单成功",username);
            //抢单成功
            return new Result(true, StatusCode.SUCCESS,"抢单成功！",bo);
        }

//        log.info("{} 系统繁忙~~~",username);
        return new Result(false,StatusCode.ERROR_RESPONSE,"服务器繁忙，请稍后再试");
    }

    @PostMapping("getOrderList")
    public Result getOrderList(@RequestBody Merge merge){
        String username = TokenDecode.getUserInfo().get("username");
//        String username = "admins";

        return new Result(true,StatusCode.SUCCESS,"查询成功",seckillOrderService.getOrderList(merge,username));
    }


    @GetMapping("/getOrderOne/{orderId}")
    public Result getOrderOne(@PathVariable("orderId") String orderId){
        String username = TokenDecode.getUserInfo().get("username");
        return new Result(true,StatusCode.SUCCESS,"查询成功",seckillOrderService.getOrderOne(orderId,username));
    }

    @PutMapping("/{orderId}/cancel")
    public Result cancelOrder(@PathVariable("orderId") String orderId,@RequestBody Merge merge){
        String username = TokenDecode.getUserInfo().get("username");
        String cancelReason = merge.getCancelReason();
        log.info("取消订单：{}",cancelReason);

        seckillOrderService.cancelOrder(orderId,cancelReason,username);
        return new Result(true,StatusCode.SUCCESS,"取消成功");
    }

    /****
     * 查询抢购
     * @return
     */
    @GetMapping(value = "/query")
    public Result queryStatus(@RequestParam String username){
        //获取用户名
//        String username = TokenDecode.getUserInfo().get("username");

        //根据用户名查询用户抢购状态
        List<SeckillStatus> seckillStatus = seckillOrderService.queryStatus(username);

        if(seckillStatus.size() != 0){
            return new Result(true,StatusCode.SUCCESS,"抢购状态",seckillStatus);
        }
        //NOTFOUNDERROR =20006,没有对应的抢购数据
        return new Result(false,StatusCode.ERROR_RESPONSE,"没有抢购信息");
    }

    public Result addOrderFallbackHandler(Merge merge) {
        log.info("SeckillOrderController addOrderFallbackHandler服务降级");
        return new Result(false,StatusCode.ERROR_RESPONSE,"系统繁忙,请稍后再试~~~~");
    }

    public static void main(String[] args) {
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer1.append("abc");

        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("abc");

        stringBuffer1 = stringBuffer2;
        stringBuffer2.append("d");

        System.out.println(stringBuffer1);
    }
}

