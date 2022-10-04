package erabbit.oauth.web.config;

import erabbit.oauth.web.handle.SecureLogoutHandler;
import erabbit.oauth.web.handle.SecureLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(-1)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DataSource dataSource;

//    @Resource
//    private SecureLogoutHandler secureLogoutHandler;

    @Autowired
    private SecureLogoutSuccessHandler secureLogoutSuccessHandler;




    /***
     * 忽略安全拦截的URL
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login/main",
                "/login/admin","/login/index");

    }


    /***
     * 创建授权管理认证对象
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    /***
     * 采用BCryptPasswordEncoder对密码进行编码
     * @return
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        String s = "oauthToken";
        String encode = passwordEncoder().encode(s);
        System.out.println(encode);
    }

//    /**
//     * 记住我持久化数据源
//     * JdbcTokenRepositoryImpl  CREATE_TABLE_SQL 建表语句可以先在数据库中执行
//     *
//     * @return
//     */
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setDataSource(dataSource);
////        jdbcTokenRepository.createNewToken();
//        //第一次会执行CREATE_TABLE_SQL建表语句 后续会报错 可以关掉
////        jdbcTokenRepository.setCreateTableOnStartup(true);
//        return jdbcTokenRepository;
//    }


    /****
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .httpBasic()        //启用Http基本身份验证
                .and()
                .formLogin()       //启用表单身份验证
                .loginPage("/login/main")         //登录地址
                .loginProcessingUrl("/login/admin")  //登录处理地址
                .and()
                .logout()
                .logoutUrl("/login/logout")
                .addLogoutHandler(new SecureLogoutHandler())
                .deleteCookies("Authorization")
                // 配置用户登出自定义处理类
                .logoutSuccessHandler(secureLogoutSuccessHandler)
//                .and()
//                .rememberMe()
//                .alwaysRemember(true)
//                .rememberMeParameter("remember-me")
//                .rememberMeCookieName("rememberme-token")
////                .authenticationSuccessHandler(rememberMeAuthenticationSuccessHandler)
//                .tokenRepository(securityUserTokenService)
                .and()
                .authorizeRequests()
                .antMatchers("/login/test")
                .permitAll()
                .and()
                .authorizeRequests()    //限制基于Request请求访问
                .anyRequest()
                .authenticated();       //其他请求都需要经过验证


    }
}
