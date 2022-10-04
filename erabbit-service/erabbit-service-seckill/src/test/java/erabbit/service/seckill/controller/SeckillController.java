package erabbit.service.seckill.controller;

import com.erabbit.seckill.pojo.dto.Merge;
import erabbit.service.seckill.SeckillApplication;
import erabbit.service.seckill.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = {SeckillApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootConfiguration
@WebAppConfiguration
public class SeckillController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderController orderController;


    @Test
    public void OrderTest(){
        Merge merge = new Merge();
        merge.setOrderState(1);


        Merge merge1 = new Merge();
        merge1.setOrderState(1);
        System.out.println(merge.equals(merge1));
//        orderService.addOrder(merge, "admins");
    }

}
