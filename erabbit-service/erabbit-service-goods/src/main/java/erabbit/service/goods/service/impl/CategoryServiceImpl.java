package erabbit.service.goods.service.impl;


import com.erabbit.seckill.pojo.Category;
import com.erabbit.seckill.pojo.Goods;
import erabbit.service.goods.mapper.CategoryMapper;
import erabbit.service.goods.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品类目 服务实现类
 * </p>
 *
 * @author erabbit_admin_111
 * @since 2022-02-20
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> getCategoryList() {
        List<Category> categoryList = categoryMapper.selectList(null);
        List<Category> categoryHeader = getCategoryHeader(categoryList, 0);
        categoryHeader.forEach(map->{
            List<Goods> goods = categoryMapper.getSpu(map.getId());
            map.setGoods(goods);
        });

        return categoryHeader;
    }

    public List<Category> getCategoryHeader(List<Category> categoryList,Integer parentId){
        List<Category> categoryLists = new ArrayList();

        for (Category category:categoryList) {
            if (parentId.equals(category.getParentId())) {
                category.setChildren(getCategoryHeader(categoryList,category.getId()));
                categoryLists.add(category);
            }
        }
        return categoryLists;
    }
}
