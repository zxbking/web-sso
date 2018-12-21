package com.zxbking.web.sso.open;

import com.zxbking.web.sso.comm.AbstractRestController;
import com.zxbking.web.sso.comm.RestResponse;
import com.zxbking.web.sso.comm.utils.JSONUtil2;
import com.zxbking.web.sso.comm.utils.PasswordUtil;
import com.zxbking.web.sso.comm.utils.UUIDGenerator;
import com.zxbking.web.sso.redis.RedisCacheService;
import com.zxbking.web.sso.dbmapper.UserMapper;
import com.zxbking.web.sso.dbmodel.User;
import com.zxbking.web.sso.dbmodel.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxibin on 2017/8/28.
 */
@RestController
@RequestMapping()
public class SsoController extends AbstractRestController{
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    UserMapper userMapper;

    /**
     * 登录接口
     * @param username 用户名
     * @param passwd 密码
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public RestResponse login(String username, String passwd){
        UserExample example =new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> list = userMapper.selectByExample(example);
        if(1 == list.size()){
            User user = list.get(0);
            String pm = PasswordUtil.springSecurityPasswordEncode(passwd, username);
            if (pm.equals(user.getPassword())) {
                //保存到redis,根据凭证找用户
                String token = "token_api_"+ UUIDGenerator.uuid();
                redisCacheService.set(token,username,30*60);//使用token为key，存用户名 超过30*60秒自动删除
                redisCacheService.setObject(username,user);//使用用户名为key存用户信息
                //请随便改造吧。返回内容
                Map map = new HashMap<>();
                map.put("token",token);
                return RestResponse.success(map);
            }
        }
        return RestResponse.failed("0020","用户名或密码不正确");
    }

    /**
     * 新增用户
     * @param username 用户名
     * @param passwd 密码
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public RestResponse add( String username, String passwd){
        User user = new User();
        user.setId(UUIDGenerator.uuid());
        user.setUsername(username);
        user.setPassword(PasswordUtil.springSecurityPasswordEncode(passwd, username));
        int re = userMapper.insert(user);
        return RestResponse.success(re);
    }
    /**
     * 登出接口
     * @return
     */
    @RequestMapping(value = "/to/logout",method = RequestMethod.GET)
    public RestResponse logout(){
        redisCacheService.del(getCurrentAccountUserName());
        return RestResponse.success("登出成功");
    }
    @RequestMapping(value = "/cur/user",method = RequestMethod.GET)
    public RestResponse token(){
        String username = getCurrentAccountUserName();
        String str = redisCacheService.get(username);
        User user = JSONUtil2.fromJson(str,User.class);
        user.setPassword("");
//        redisCacheService.set(token,username,30*60);//使用token为key，存用户名 超过30*60秒自动删除
//        redisCacheService.setObject(username,user);/
//        redisCacheService.del(getCurrentAccountUserName());
        return RestResponse.success(user);
    }

}
