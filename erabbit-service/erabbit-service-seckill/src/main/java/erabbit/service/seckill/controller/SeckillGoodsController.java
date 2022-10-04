package erabbit.service.seckill.controller;


import com.erabbit.common.entity.DateUtil;
import com.erabbit.common.entity.IdWorker;
import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import erabbit.service.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 秒杀商品信息表 前端控制器
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    /*****
     * 获取时间菜单
     */
    @RequestMapping(value = "/menus")
    public List<Date> dateMenus(){
        return DateUtil.getDateMenus();
    }

    /****
     * 对应时间段秒杀商品集合查询
     * 调用Service查询数据
     * @param time:2019050716
     */
//    @RequestMapping(value = "/list")
//    public List<GoodsDetail> list(@PathVariable("time") String time){
//        //调用Service查询数据
//        return seckillGoodsService.list(time);
//    }
    public static void main(String[] args) {
        IdWorker idWorker = new IdWorker();
        System.out.println(idWorker.nextId());
    }


    /****
     * 根据ID查询商品详情
     * 调用Service查询商品详情
     * @param time
     * @param seckillId
     */
    @GetMapping(value = "/getOne")
    public GoodsDetail one(@RequestParam("time") String time,@RequestParam("seckillId") Long seckillId){
        //调用Service查询商品详情
        return seckillGoodsService.one(time,seckillId);
    }
}

