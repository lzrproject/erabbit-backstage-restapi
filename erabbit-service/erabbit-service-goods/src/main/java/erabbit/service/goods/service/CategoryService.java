package erabbit.service.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erabbit.seckill.pojo.Category;

import java.util.List;

/**
 * <p>
 * 商品类目 服务类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-02-20
 */
public interface CategoryService extends IService<Category> {

    List<Category> getCategoryList();
}
