package com.zxbking.web.sso.security;

import com.zxbking.web.sso.redis.RedisCacheService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxibin on 2016/6/7.
 */
@Service("preUserDetailsService")
public class PreUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    public static final Logger logger = LoggerFactory.getLogger(PreUserDetailsService.class);
    @Autowired
    private RedisCacheService cacheManager;

    private List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority( role));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        if(logger.isDebugEnabled()){
            logger.debug("验证身份Token：{}",token);
        }
        String principal = (String) token.getPrincipal();
        User user = null;
        if(StringUtils.isNotBlank(principal)) {
            //此处应根据token从Redis获取用户并组装成UserDetails返回（principal为tocken） AbstractAuthenticationToken
            String username = cacheManager.get(principal);
            if(StringUtils.isNotBlank(username)){
                com.zxbking.web.sso.dbmodel.User user1 = cacheManager.getObject(username, com.zxbking.web.sso.dbmodel.User.class);
                if( null != user1){
                    logger.info("请求用户：{}",username);
                    //此处时间设置得比portal的session存在时间稍微长一点
                    cacheManager.expire(username,35*60);
                    cacheManager.expire(principal,35*60);
                    user = new User(user1.getUsername(),user1.getPassword(),true,true,true,true,roles("ROLE_OC_USER"));
                }else{
                    user = null;

                }

            }
        }
        if(user == null){
            cacheManager.del(principal);
            throw new UsernameNotFoundException(principal);

        }
        if(logger.isDebugEnabled()){
            logger.debug("验证身份通过：{}->{}",token,user.getUsername());
        }
        return user;
    }

}
