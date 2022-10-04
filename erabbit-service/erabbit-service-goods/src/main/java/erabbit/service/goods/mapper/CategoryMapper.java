package erabbit.service.goods.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.seckill.pojo.Category;
import com.erabbit.seckill.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 商品类目 Mapper 接口
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-02-20
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Goods> getSpu(Integer categoryId);
}
