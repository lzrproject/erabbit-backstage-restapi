package erabbit.oauth.web.config;

import com.erabbit.common.entity.Result;
import com.erabbit.user.feign.UserFeign;
import erabbit.oauth.web.controller.LoginController;
import erabbit.oauth.web.util.UserJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/*****
 * 自定义授权认证类
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private UserFeign userFeign;

    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /***
         * 客户端信息认证
         */
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
                //return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                //数据库查找方式
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }

        /***
         * 用户信息认证
         */
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        Result<com.erabbit.user.pojo.User> userResult = userFeign.findUsername(username);
        if (userResult.getData() == null) return null;
        com.erabbit.user.pojo.User user = userResult.getData();
//        log.info("threadLocal get：{}",Thread.currentThread().getId());
//        com.erabbit.user.pojo.User user = LoginController.map.get();

        //根据用户名查询用户信息
        String password = user.getPassword();

        //获取权限信息
//        String permissions = data.getPermissionCode();

        UserJwt userDetails = new UserJwt(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList(null));
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
//        userDetails.setNickname(data.getNickname());
        return userDetails;
    }
}
