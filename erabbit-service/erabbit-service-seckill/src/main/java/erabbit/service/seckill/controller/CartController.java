package erabbit.service.seckill.controller;

import com.erabbit.seckill.pojo.Order;
import com.erabbit.seckill.pojo.OrderItem;
import com.erabbit.seckill.pojo.dto.Merge;
import erabbit.service.seckill.config.TokenDecode;
import erabbit.service.seckill.service.CartService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;
//    private String username = "admins";

    /**
     * 合并购物车
     * @Param
     */
    @PostMapping("/merge")
    public void merge(@RequestBody List<Merge> cartList){
        String username = TokenDecode.getUserInfo().get("username");
        cartService.addCart(cartList,username);
    }

    @PostMapping("/addCart")
    public void addCart(@RequestBody Merge merge){

        String username = TokenDecode.getUserInfo().get("username");
        cartService.addCartOne(merge,username);
//        return new Result(true, StatusCode.OK,"加入购物车成功");
    }

    /**
     * 获取购物车
     * @return
     */
    @GetMapping("/list")
    public List<Order> list(){
        String username = TokenDecode.getUserInfo().get("username");
        return cartService.list(username);
    }

    @DeleteMapping("delete")
    public void delCart(@RequestBody Merge merge){
        String username = TokenDecode.getUserInfo().get("username");
        cartService.delCart(merge.getSkuIds(),username);
    }



}
