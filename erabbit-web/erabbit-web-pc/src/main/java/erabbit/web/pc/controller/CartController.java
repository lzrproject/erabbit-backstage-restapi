package erabbit.web.pc.controller;

import com.erabbit.seckill.feign.CartFeign;
import com.erabbit.seckill.pojo.Order;
import com.erabbit.seckill.pojo.OrderItem;
import com.erabbit.seckill.pojo.dto.Merge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/member")
@RestController
public class CartController {

    @Autowired
    private CartFeign cartFeign;

    @PostMapping("/cart/merge")
    public void mergeCart(@RequestBody List<Merge> cartList){
        cartFeign.merge(cartList);
    }

    @PostMapping("addCart")
    public void addCart(@RequestBody Merge merge){
        cartFeign.addCart(merge);
    }

    @GetMapping("cart")
    public List<Order> getCart(){
        return cartFeign.list();
    }

    @PostMapping("delCart")
    public void delCart(@RequestBody Merge merge){
        cartFeign.delCart(merge);
    }
}
