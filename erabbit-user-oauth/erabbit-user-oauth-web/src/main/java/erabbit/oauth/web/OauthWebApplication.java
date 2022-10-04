package erabbit.oauth.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"erabbit.oauth.web","com.erabbit.user.handle"})
@EnableDiscoveryClient
@EnableAuthorizationServer
@EnableFeignClients(basePackages = {"com.erabbit.user.feign"})
public class OauthWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthWebApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
