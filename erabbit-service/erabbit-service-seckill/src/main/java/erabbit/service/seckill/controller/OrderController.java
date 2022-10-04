package erabbit.service.seckill.controller;

import com.alibaba.fastjson.JSONObject;
import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.erabbit.seckill.pojo.NewOrder;
import com.erabbit.seckill.pojo.dto.Merge;
import erabbit.service.seckill.config.TokenDecode;
import erabbit.service.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {

    public static void main(String[] args) {
        Connection connection=null;
        ResultSet resultSet=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://118.25.242.174:3306/erabbit_data?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai",
                    "root", "123456");
            resultSet = connection.prepareStatement("select * from tb_user").executeQuery();
//            resultSet.getMetaData()
            System.out.println(111);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Autowired
    private OrderService orderService;

    @GetMapping("getCart")
    public Map<String,Object> getCart(){
        String username = TokenDecode.getUserInfo().get("username");

        return orderService.getCart(username);
    }

    @PostMapping("addOrder")
    public Result addOrder(@RequestBody Merge merge){
        String username = TokenDecode.getUserInfo().get("username");
        return new Result(true, StatusCode.SUCCESS, "提交成功",orderService.addOrder(merge,username));
    }
}
