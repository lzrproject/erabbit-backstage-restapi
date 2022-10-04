package erabbit.service.seckill.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.erabbit.seckill.pojo.*;
import erabbit.service.seckill.mapper.CategoryMapper;
import erabbit.service.seckill.mapper.SeckillGoodsMapper;
import erabbit.service.seckill.mapper.SkuMapper;
import erabbit.service.seckill.mapper.SpuMapper;
import erabbit.service.seckill.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public GoodsDetail getOne(Long id) {
        SeckillGoods seckillGoods = seckillGoodsMapper.selectById(id);

        Long supId = seckillGoods.getSupId();
        Spu spu = spuMapper.getOne(supId);
        Map<String,Object> map = new HashMap<>();
        map.put("spu_id", supId);
        List<Sku> skus = skuMapper.selectByMap(map);

        return this.bySpu(seckillGoods,spu,skus);
    }

    public GoodsDetail bySpu(SeckillGoods seckillGoods,Spu spu,List<Sku> skus){
        GoodsDetail goodsDetail = new GoodsDetail();
        Integer num = seckillGoods.getNum();
        Integer stockCount = seckillGoods.getStockCount();
        int salesCount = num - stockCount;
        int salesPer = new BigDecimal((float) salesCount / num).setScale(2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")).intValue();

        goodsDetail.setId(seckillGoods.getSkuId().toString());
        goodsDetail.setCategories(spu.getCategories());
        goodsDetail.setCollectCount(999);
        goodsDetail.setCommentCount(888);
        goodsDetail.setDesc(seckillGoods.getIntroduction());
        goodsDetail.setInventory(stockCount);
        goodsDetail.setIsCollect(false);
        goodsDetail.setMainPictures(Arrays.asList(StringUtils.split(spu.getImages(), ",")));
        goodsDetail.setName(seckillGoods.getName());
        goodsDetail.setOldPrice(seckillGoods.getPrice().toString());
        goodsDetail.setPrice(seckillGoods.getCostPrice().toString());
//        goodsDetail.setSalesCount(salesCount);

        /**
         * 封装sku
         */
        List<TSku> list = new ArrayList<>();
        for (Sku sku : skus) {
            String id = sku.getId();
            TSku tSku = new TSku();
            tSku.setId(id);
            tSku.setInventory(sku.getNum());
            tSku.setSkuCode(id);
            tSku.setSpecs(JSONArray.parseArray(sku.getSpec(),Object.class));
            if (seckillGoods.getSkuId().toString().equals(id)){
                tSku.setPrice(seckillGoods.getCostPrice().toString());
                tSku.setOldPrice(seckillGoods.getPrice().toString());
            }else {
                tSku.setOldPrice(sku.getPrice().toString());
                tSku.setPrice(sku.getPrice().toString());
            }
            list.add(tSku);
        }
        goodsDetail.setSkus(list);
        goodsDetail.setSpecs(JSONArray.parseArray(spu.getSpecItems()));

        goodsDetail.setNum(num);
        goodsDetail.setStartTime(seckillGoods.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHH")));
        goodsDetail.setEndTime(seckillGoods.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHH")));
        goodsDetail.setSmallPic(seckillGoods.getSmallPic());
        goodsDetail.setSalesPer(salesPer);
        goodsDetail.setSeckillId(String.valueOf(seckillGoods.getId()));
        return goodsDetail;
    }

}
