package erabbit.service.goods.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erabbit.seckill.pojo.Spu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpuMapper extends BaseMapper<Spu> {
    Spu getOne(Long supId);
}
