package com.zxbking.web.sso.security;

import com.zxbking.web.sso.comm.utils.UUIDGenerator;
import com.zxbking.web.sso.redis.RedisCacheService;
import com.zxbking.web.sso.dbmapper.UserMapper;
import com.zxbking.web.sso.dbmodel.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxibin on 2016/6/7.
 */
@Service("userDetailsService")
public class APIUserDetailsService implements UserDetailsService{
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisCacheService redisCacheService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<com.zxbking.web.sso.dbmodel.User> list = userMapper.selectByExample(example);
        if (1 == list.size()) {
            com.zxbking.web.sso.dbmodel.User user1 = list.get(0);
            //保存到redis,根据凭证找用户
            String token = UUIDGenerator.javaId();
            redisCacheService.set(token, username, 30 * 60);//使用token为key，存用户名 超过30*60秒自动删除
            redisCacheService.setObject(username, user1);//使用用户名为key存用户信息
            User user = new User(user1.getUsername(), user1.getPassword(), true, true, true, true, roles("ROLE_OC_USER"));
            return user;
        } else{
            throw new UsernameNotFoundException("用户不存在");
        }
    }



    private List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
                roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority( role));
        }
        return authorities;
    }
}
