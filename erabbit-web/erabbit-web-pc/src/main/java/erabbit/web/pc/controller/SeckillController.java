package erabbit.web.pc.controller;

import com.erabbit.common.entity.DateUtil;
import com.erabbit.common.entity.Result;
import com.erabbit.seckill.feign.GoodsFeign;
import com.erabbit.seckill.feign.SecKillGoodsFeign;
import com.erabbit.seckill.pojo.GoodsDetail;
import com.erabbit.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("webSeckill")
public class SeckillController {
    @Autowired
    private SecKillGoodsFeign secKillGoodsFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    //获取秒杀时间段集合信息
    @GetMapping("/timeMenus")
    public List<String> dateMenus(){

        //获取当前时间段相关的信息集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        List<String> result = new ArrayList<>();

        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }

        return  result;

    }
    @GetMapping("/getSeckillList")
    public List<GoodsDetail> list(@RequestParam("time") String time){
        List<GoodsDetail> goodsList = goodsFeign.list(DateUtil.formatStr(time));
        if (goodsList.size() > 4){
            return goodsList.subList(0,4);
        }
        return goodsList;
    }

    /**
     * 秒杀详情页
     * @param time
     * @param seckillId
     * @return
     */
//    @GetMapping("getOne")
//    public GoodsDetail getOne(@RequestParam("time") String time,@RequestParam("seckillId") Long seckillId){
//        return secKillGoodsFeign.one(time,seckillId);
//    }

}
