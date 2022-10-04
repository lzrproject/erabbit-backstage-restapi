package erabbit.oauth.web.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminToken {


    @Value("${auth.location-file}")
    private String locationFile;

    @Value("${encrypt.key-store.alias}")
    private String alias;

    @Value("${encrypt.key-store.secret}")
    private String secret;

    @Value("${encrypt.key-store.password}")
    private String password;

    /***
     * 管理员令牌发放
     * @return
     */
    public String adminToken(){
        //加载证书 读取证书路径
        ClassPathResource classPathResource = new ClassPathResource(locationFile);

        //读取证书 加载证书数据
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,password.toCharArray());

        //获取公钥 私钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,password.toCharArray());

        //获取私钥->RSA算法
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //创建令牌，需要私钥加盐【RSA算法】
        Map<String,Object> payload = new HashMap<String, Object>();
        payload.put("address","sz");
        payload.put("email","123");
        payload.put("name","tom");
        payload.put("authorities",new String[]{"admin","oauth"});

        Jwt encode = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));

        String token = encode.getEncoded();
        return token;
    }

}
