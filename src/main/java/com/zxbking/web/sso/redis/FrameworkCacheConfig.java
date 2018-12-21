package com.zxbking.web.sso.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxibin on 2016/6/25.
 */
@ComponentScan
@EnableCaching
@Configurable
@Configuration
@EnableAutoConfiguration
public class FrameworkCacheConfig extends CachingConfigurerSupport {

    public static final Logger logger = LoggerFactory.getLogger(FrameworkCacheConfig.class);
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
    @Bean
    @ConfigurationProperties(prefix="spring.redis")
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }

    @Bean(name="lsxyRedisTemplate")
    @ConfigurationProperties(prefix="spring.redis")
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory) {
        final RedisTemplate template = new RedisTemplate();
        template.setKeySerializer(template.getStringSerializer());
        template.setHashKeySerializer(template.getStringSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(factory);
        JedisConnectionFactory obj  = (JedisConnectionFactory)template.getConnectionFactory();
        logger.info("Redis-IP:"+obj.getHostName());
        logger.info("Redis-Port:"+obj.getPort());
        logger.info("Redis-password:"+obj.getPassword());
        return template;
    }

    @Bean(name="businessRedisTemplate")
    public RedisTemplate<String, String> hashAndSetRedisTemplate(
            RedisConnectionFactory factory) {
        final RedisTemplate template = new RedisTemplate();
        template.setKeySerializer(template.getStringSerializer());
        template.setHashKeySerializer(template.getStringSerializer());
        template.setHashValueSerializer(template.getStringSerializer());
        template.setValueSerializer(template.getStringSerializer());
        template.setConnectionFactory(factory);
        JedisConnectionFactory obj  = (JedisConnectionFactory)template.getConnectionFactory();
        logger.info("Redis-IP:"+obj.getHostName());
        logger.info("Redis-Port:"+obj.getPort());
        logger.info("Redis-password:"+obj.getPassword());
        return template;
    }

    @Bean
    public CacheManager cacheManager(@Qualifier("lsxyRedisTemplate") RedisTemplate lsxyRedisTemplate,
                                     @Value("${cache.redis.expires}") String expiress
    ) {
        RedisCacheManager rcm = new RedisCacheManager(lsxyRedisTemplate);
        Map<String,String> expires = new HashMap<>();
        if(StringUtils.isNotEmpty(expiress)){
            String items[] = expiress.split(";");
            for (String item:items ) {
                int idx = item.indexOf(":");
                if( idx >0){
                    expires.put(item.substring(0,idx),item.substring(idx + 1));
                }
            }
        }
        Map<String,Long> expiresLong = new HashedMap();
        for (String key:expires.keySet()) {
            Long expireValue = Long.parseLong(expires.get(key));
            if (logger.isDebugEnabled()){
                    logger.debug("设置缓存过期时间->{}:{}",key,expireValue);
             }
            expiresLong.put(key,expireValue);
        }
        rcm.setExpires(expiresLong);

        return rcm;
    }


}
