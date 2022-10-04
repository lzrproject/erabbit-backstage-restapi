package com.erabbit.oauth2.config;

import com.erabbit.oauth2.controller.common.SecureCaptchaSupport;
import com.erabbit.oauth2.handle.SecureAuthenticationEntryPoint;
import com.erabbit.oauth2.handle.SecureLogoutHandler;
import com.erabbit.oauth2.handle.SecureLogoutSuccessHandler;
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
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    @Autowired
    private SecureCaptchaSupport secureCaptchaSupport;

    @Autowired
    private SecureAuthenticationEntryPoint secureAuthenticationEntryPoint;

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
        web.ignoring().antMatchers("/login/main","/admin/**","/component/**","pear.config.yml","/system/captcha/**",
                "/login/admin");

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
        String s = "erabbit";
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

        http.authorizeRequests()    //限制基于Request请求访问
                .anyRequest().authenticated()       //其他请求都需要经过验证
                .and()
                // 验证码验证类
                .addFilterBefore(secureCaptchaSupport, UsernamePasswordAuthenticationFilter.class)
                .httpBasic()            //启用Http基本身份验证
                .authenticationEntryPoint(secureAuthenticationEntryPoint)
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
                .logoutSuccessHandler(secureLogoutSuccessHandler);
//                .and()
//                .rememberMe()
//                .alwaysRemember(true)
//                .rememberMeParameter("remember-me")
//                .rememberMeCookieName("rememberme-token")
////                .authenticationSuccessHandler(rememberMeAuthenticationSuccessHandler)
//                .tokenRepository(securityUserTokenService)


        http.csrf().disable();
        // 防止iframe 造成跨域
        http.headers().frameOptions().disable();
    }
}
