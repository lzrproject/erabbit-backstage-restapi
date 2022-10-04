package erabbit.oauth.web.controller;

import com.erabbit.common.entity.Result;
import erabbit.oauth.web.OauthWebApplication;
import erabbit.oauth.web.entity.vo.UserDetails;
import erabbit.oauth.web.util.AuthToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;

@SpringBootTest(classes = OauthWebApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class LoginTestController {

    @Autowired
    private LoginController loginController;

    @Test
    public void test(){
        writeToken(1000);
    }

//    @Test
    public void writeToken(Integer num){
        File file = new File("E:\\idea\\jmeter-test\\token.txt");
        if (file.exists()) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "GBK"));
            for (int i = 1; i <= num; i++) {
                UserDetails details = new UserDetails();
                details.setAccount("user"+i);
                details.setPassword("admins");
                Result login = loginController.login(details);
                AuthToken authToken = (AuthToken) login.getData();
                writer.write(authToken.getAccessToken()+"\n");
                System.out.println("写入文件：user"+i);
            }

            writer.flush();
            writer.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
