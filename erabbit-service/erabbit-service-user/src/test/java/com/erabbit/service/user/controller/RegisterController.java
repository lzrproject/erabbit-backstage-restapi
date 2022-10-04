package com.erabbit.service.user.controller;

import com.erabbit.common.entity.Result;
import com.erabbit.service.user.UserApplication;
import com.erabbit.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;
import java.util.ArrayList;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RegisterController {

    @Autowired
    private UserController userController;

    @Test
    public void test(){

        Integer num = 5000;
        batchRegister(num);
//        readFile(num);


    }

    public void batchRegister(Integer num){
        for (int i = 1; i <= num; i++) {
            User user = new User();
            user.setId(1000000+i);
            user.setUsername("user"+i);
            user.setPassword("admins");
            user.setRemark("小兔鲜秒杀测试号"+i);
            System.out.println(userController.pcAdd(user));
        }
    }

    /**
     * 用户账号密码
     * @param num
     */
    public void readFile(Integer num){
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("asd");
        String filePath = "E:\\idea\\jmeter-test\\user.txt";
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        try {
//            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "GBK"));
            for (int i = 1; i <= num; i++) {
                bufferedWriter.write("user"+i+",admins\n");
                System.out.println("写入文件：user"+i);
            }

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
