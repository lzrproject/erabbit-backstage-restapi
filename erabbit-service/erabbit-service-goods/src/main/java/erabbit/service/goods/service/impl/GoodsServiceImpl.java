package erabbit.service.goods.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.erabbit.seckill.pojo.*;
import erabbit.service.goods.mapper.SeckillGoodsMapper;
import erabbit.service.goods.mapper.SkuMapper;
import erabbit.service.goods.mapper.SpuMapper;
import erabbit.service.goods.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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



    @Override
    public GoodsDetail getOne(Long supId) {
        Spu spu = spuMapper.getOne(supId);
        Map<String,Object> map = new HashMap<>();
        map.put("spu_id", supId);
        List<Sku> skus = skuMapper.selectByMap(map);

        return this.bySpu(spu,skus);
    }

    public GoodsDetail bySpu(Spu spu,List<Sku> skus){
        GoodsDetail goodsDetail = new GoodsDetail();
//        Integer num = seckillGoods.getNum();
//        Integer stockCount = seckillGoods.getStockCount();
//        int salesCount = num - stockCount;
//        int salesPer = new BigDecimal((float) salesCount / num).setScale(2, BigDecimal.ROUND_HALF_UP)
//                .multiply(new BigDecimal("100")).intValue();

        Sku sku1 = skus.get(0);

        goodsDetail.setId(sku1.getId());
        goodsDetail.setCategories(spu.getCategories());
        goodsDetail.setCollectCount(999);
        goodsDetail.setCommentCount(888);
        goodsDetail.setDesc(spu.getIntroduction());
        goodsDetail.setInventory(sku1.getNum());
        goodsDetail.setIsCollect(false);
        goodsDetail.setMainPictures(Arrays.asList(StringUtils.split(spu.getImages(), ",")));
        goodsDetail.setName(sku1.getName());
        goodsDetail.setOldPrice(sku1.getPrice().toString());
        goodsDetail.setPrice(sku1.getPrice().toString());
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
            tSku.setOldPrice(sku.getPrice().toString());
            tSku.setPrice(sku.getPrice().toString());
            list.add(tSku);
        }
        goodsDetail.setSkus(list);
        goodsDetail.setSpecs(JSONArray.parseArray(spu.getSpecItems()));

//        goodsDetail.setNum(num);
//        goodsDetail.setStartTime(seckillGoods.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHH")));
//        goodsDetail.setEndTime(seckillGoods.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHH")));
//        goodsDetail.setSmallPic(seckillGoods.getSmallPic());
//        goodsDetail.setSalesPer(salesPer);
//        goodsDetail.setSeckillId(String.valueOf(seckillGoods.getId()));
        return goodsDetail;
    }

}
