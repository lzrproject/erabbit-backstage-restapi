package com.erabbit.oauth2.handle;//package com.dataprocess.oauth2.handle;
//
//import com.ctd.springboot.auth.utils.auth.AuthUtils;
//import com.ctd.springboot.common.core.vo.response.ResponseVO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.util.Assert;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Objects;
//
///**
// * OauthLogoutHandler
// *
// * @author chentudong
// * @date 2020/3/14 14:38
// * @since 1.0
// */
//public class OauthLogoutHandler implements LogoutHandler
//{
//    private static final Logger LOGGER = LoggerFactory.getLogger(OauthLogoutHandler.class);
//    @Autowired
//    private TokenStore tokenStore;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//    {
//        Assert.notNull(tokenStore, "tokenStore must be set");
//        String token = request.getParameter("token");
//        if (StringUtils.isBlank(token))
//        {
//            token = AuthUtils.extractToken(request);
//        }
//        if (StringUtils.isNotBlank(token))
//        {
//            OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
//            OAuth2RefreshToken refreshToken;
//            if (Objects.nonNull(existingAccessToken))
//            {
//                if (Objects.nonNull(existingAccessToken.getRefreshToken()))
//                {
//                    LOGGER.info("remove refreshToken! {}", existingAccessToken.getRefreshToken());
//                    refreshToken = existingAccessToken.getRefreshToken();
//                    tokenStore.removeRefreshToken(refreshToken);
//                }
//                LOGGER.info("remove existingAccessToken! {}", existingAccessToken);
//                tokenStore.removeAccessToken(existingAccessToken);
//            }
//        }
//        try
//        {
//            ResponseVO.responseWriter(objectMapper, response, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
