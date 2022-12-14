package erabbit.oauth.web.controller;

import com.erabbit.common.entity.Result;
import com.erabbit.common.entity.StatusCode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import erabbit.oauth.web.entity.vo.UserDetails;
import erabbit.oauth.web.service.UserLoginService;
import erabbit.oauth.web.util.AuthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.erabbit.user.pojo.User;
import com.erabbit.user.feign.UserFeign;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("login")
@Slf4j
@Component
@RefreshScope
public class LoginController {

    public static final ThreadLocal<User> map = new ThreadLocal<>();

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private UserLoginService userLoginService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${test.test}")
    private String val;

    @GetMapping("test")
    @ResponseBody
    public String test(){
        return val;
    }

    /**
     * ????????????
     * @param userDetails
     * @return
     * @throws Exception
     */
    @PostMapping("admin")
    @ResponseBody
    @HystrixCommand(fallbackMethod = "loginFallbackHandler",commandProperties = {
            //??????????????????paymentInfoTimeout??????????????????????????????5000?????????5s??????????????????????????????
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "10000")
    },threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "50"),
            @HystrixProperty(name = "maxQueueSize", value = "100"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100")
    })
    public Result login(@RequestBody UserDetails userDetails) throws Exception {
        String username = userDetails.getAccount();
        String password = userDetails.getPassword();

        log.info("username???{}???password???{}",username,password);

        Result<User> userAll = userFeign.findUsername(username);
        log.info("?????????feign???????????????");

        User data = null;
        if (userAll != null)
            data = userAll.getData();
        if (data == null){
            return new Result(false, StatusCode.ERROR_PRIVILEGE,"????????????");
        }else if (data.getEnable() == 0){
            return new Result(false, StatusCode.ERROR_PRIVILEGE,"?????????????????????????????????");
        }

//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        boolean matches = bCryptPasswordEncoder.matches(password, data.getPassword());


//        if (!matches) {
//            return new Result(false, StatusCode.ERROR_PRIVILEGE,"????????????");
//        }
        log.info("threadLocal set?????????{}",Thread.currentThread().getId());
        data.setPassword(password);
        map.set(data);

        AuthToken authToken = (AuthToken) redisTemplate.boundValueOps("authorization_"+username).get();

        log.info("Redis???????????????{}",authToken);

        if (authToken == null){
            authToken = userLoginService.login(clientId, clientSecret, "password");
            redisTemplate.boundValueOps("authorization_"+username).set(authToken,720000, TimeUnit.SECONDS);
        }

        //token??????cookie
//        if (authToken != null) {
//            saveCookie(authToken.getAccessToken());
//        }
        return new Result(true, StatusCode.SUCCESS,"????????????",authToken);
    }



    @GetMapping("register")
    public Result register(@RequestParam("username") String username,@RequestParam("password") String password){

        Result<User> user = userFeign.findUsername(username);
        User data = user.getData();
        if (data != null){
            return new Result(false, StatusCode.SUCCESS,"???????????????");
        }
        return null;
    }

    public Result loginFallbackHandler(UserDetails userDetails){
        log.info("LoginController login???????????????",userDetails.getUsername());
        return new Result<>(false,StatusCode.ERROR_RESPONSE,"????????????",null);
    }

    public static void main(String[] args) {
//        Object[][] strArr = {
//                {"??????","??????",92},{"??????","??????",75},{"??????","??????",92},
//                {"??????","??????",95},{"??????","??????",87},{"??????","??????",96}
//        };
//
//        ArrayList<Map<String, Object>> list = new ArrayList<>();
//        for (int i = 0; i < strArr.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("student",strArr[i][0]);
//            map.put("title",strArr[i][1]);
//            map.put("score",strArr[i][2]);
//            list.add(map);
//        }
//
//        HashMap<String, Integer> hashMap = new HashMap<>();
//        List<Object> collect = list.stream().filter(map ->
//                (int) map.get("score") > 90
//        ).collect(Collectors.groupingBy(map -> map.get("student"), Collectors.counting()))
//                .entrySet().stream().filter(map -> map.getValue() >= 2)
//                .map(Map.Entry::getKey).collect(Collectors.toList());
//
//        System.out.println(collect);

        String content = "\"access_token\": \"a-sdasd2asd.f2da\"";
        String pattern = "\"access_token\":\\s*\"(.*?)\"";

        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(content);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
