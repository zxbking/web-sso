package com.zxbking.web.sso.redis;

import com.zxbking.web.sso.comm.utils.JSONUtil2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Redis操作方法
 * @author zhangxibin 2016/6/29
 *
 */
@Component
@DependsOn("businessRedisTemplate")
@SuppressWarnings({"unchecked","rawtypes"})
public class RedisCacheService {

    //加锁标志
//    public static final String LOCKED = "TRUE";
//    public static final long ONE_MILLI_NANOS = 1000000L;
    //默认超时时间（毫秒）
//    public static final long DEFAULT_TIME_OUT = 3000;

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

//	public static final String LOCK = LuaUtils.load("/lua/lock.lua");

    private static final ConcurrentHashMap<String,Future<String>> lua_script_cache = new ConcurrentHashMap<>();

    @Autowired
    @Qualifier("businessRedisTemplate")
    private RedisTemplate redisTemplate;

    private static String redisCode = "utf-8";

    @PostConstruct
    public void init(){

    }

    private String getSha1Promise(Jedis jedis, String script){
        String sha1Id = null;
        Future<String> f = lua_script_cache.get(script);
        if(f == null){
            FutureTask<String> task = new FutureTask<>(new Callable<String>(){
                @Override
                public String call() throws Exception {
                    logger.info("load redis script:\n{}",script);
                    return jedis.scriptLoad(script);
                }
            });
            Future<String> f1 = lua_script_cache.putIfAbsent(script, task);
            if(f1 == null){
                task.run();
                f = task;
            }else{
                f = f1;
            }
        }
        try {
            sha1Id = f.get();
        } catch (Throwable e) {
            logger.info("获取redis script sha1失败",e);
        }
        return sha1Id;
    }

    private Object eval(final Jedis jedis, final String script,final int keyCount,final String... params){
        long start = 0;
        if(logger.isDebugEnabled()){
            start = System.currentTimeMillis();
        }
        Object obj = null;
        String sha1Id = getSha1Promise(jedis, script);
        if(sha1Id != null){
            obj = jedis.evalsha(sha1Id,keyCount,params);
        }else{
            obj = jedis.eval(script,keyCount,params);
        }
        if(logger.isDebugEnabled()){
            logger.info("执行redis script耗时={}",(System.currentTimeMillis() - start));
        }
        return obj;
    }

    public Object eval(final String script,final int keyCount,final String... params){
        return redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return eval((Jedis)connection.getNativeConnection(),script,keyCount,params);
            }
        });
    }

    public Object eval(final String script){
        return redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return eval((Jedis)connection.getNativeConnection(),script,0);
            }
        });
    }

    /**
     * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
     * @param key
     * @param value
     * @throws TransactionExecFailedException
     */
    public void setTransactionFlag(final String key, final String value,final long expire)
            throws TransactionExecFailedException {
        boolean result = (boolean) redisTemplate
                .execute(new RedisCallback() {

                    /*@Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        if(logger.isDebugEnabled()){
                            logger.debug("ready to set nx:"+key+">>>>"+ value);
                        }
                        boolean ret = connection.setNX(key.getBytes(), value.getBytes());
                        if(ret){//防止没获取到锁也能刷新锁的过期时间
                            //默认缓存2天
                            connection.expire(key.getBytes(), expire);
                        }
                        if(logger.isDebugEnabled()){
                            logger.debug("set nx result:"+ret);
                        }
                        return ret;
                    }*/
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        if(logger.isDebugEnabled()){
                            logger.debug("ready to set nx:"+key+">>>>"+ value);
                        }
                        Long ret = null;//(Long)eval((Jedis)connection.getNativeConnection(), LOCK,1,key,value,""+expire);
                        if(logger.isDebugEnabled()){
                            logger.debug("set nx result:"+ret);
                        }
                        return ret != null && ret == 1;
                    }
                });
        //如果结果为空表示设置失败了
        if(result == false)
            throw new TransactionExecFailedException();
    }

    /**
     * @param keys
     */

    public long del(final String... keys) {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    /**
     * @param key
     * @param liveTime
     */
    public void expire(final byte[] key,final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime  过期时间单位为秒
     */
    public void set(String key, String value, long liveTime) {
        this.set(key.getBytes(), value.getBytes(), liveTime);
    }

    /**
     * @param key
     * @param liveTime  过期时间单位为秒
     */
    public void expire(String key, long liveTime) {
        this.expire(key.getBytes(), liveTime);
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {

        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @return
     */
    public String get(final String key) {
        String result = (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] obj = connection.get(key.getBytes());
                    if(obj != null){
                        return new String(obj, redisCode);
                    }else{
                        return null;
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("不支持的编码转换",e);
                }
                return null;
            }
        });
        logger.debug("get cache value:"+key+"=>"+result);
        return result;
    }

    public String getAndSet(final String key,final String value) {
        String result = (String) redisTemplate.boundValueOps(key).getAndSet(value);
        return result;
    }

    public String getAndSet(final String key,final String value,long expires) {
        String result = (String) redisTemplate.boundValueOps(key).getAndSet(value);
        redisTemplate.expire(key,expires, TimeUnit.SECONDS);
        return result;
    }

    /**
     * @param pattern
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    /**
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return (boolean) redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    /**
     * @return
     */
    public String flushDB() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * @return
     */
    public long dbSize() {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * @return
     */
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
    }

    /**
     * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
     * @param key
     * @param value
     * @throws TransactionExecFailedException
     */
    public void setTransactionFlag(final String key, final String value)
            throws TransactionExecFailedException {
        this.setTransactionFlag(key,value,48*60*60);
    }


    /**
     * 针对redis incr命令的封装，实现指定key的值自增长
     * @param key
     * 	key值
     * @return
     *  自增长后的值
     */
    public long incr(final String key) {
        long result = (long) redisTemplate
                .execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        return connection.incr(key.getBytes());
                    }

                });
        return result;
    }

    /**
     * 针对redis INCRBY，实现指定key的值的增长
     * @param key 	key值
     * @param incr 增长的值
     * @return
     *  自增长后的值
     */
    public long incrBy(final String key,Long incr) {
        long result = (long) redisTemplate
                .execute(new RedisCallback() {
                    @Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        return connection.incrBy(key.getBytes(),incr);
                    }

                });
        return result;
    }

    /**
     * 获取缓存的值，之后迅速删除掉
     * @param key
     * 	缓存key
     * @return
     * 	返回指定key对应的值
     */
    public String getAndRemove(final String key) {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] obj = connection.get(key.getBytes());
                    if(obj != null){
                        connection.del(key.getBytes());
                        return new String(obj, redisCode);
                    }else{
                        return null;
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("不支持的编码转换",e);
                }
                return null;
            }
        });
    }

    /**
     *
     * @param key
     * @param value
     */
    public void zrem(final String key, final String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public void zrem(String key, Object... array) {
        redisTemplate.opsForZSet().remove(key, array);

    }

    public long zsize(String key){
        return redisTemplate.opsForZSet().size(key);
    }

    public Set zReverseRange(final String key, final long start, final long end){
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Set zRange(final String key, final long start, final long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Set zRangeScore(final String key, final long min, final long max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public void zadd(final String key,final String value,double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Double zScore(final String key,final String value) {
        return redisTemplate.opsForZSet().score(key,value);
    }

    public Long zCount(final String key,final double score1,double score2) {
        return redisTemplate.opsForZSet().count(key,score1,score2);
    }

    public void sremove(final String key,final String... value){
        redisTemplate.opsForSet().remove(key,value);
    }

    public void sadd(final String key, final Object ... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public Set smembers(final String key) {
        Set set = redisTemplate.opsForSet().members(key);
        return set;
    }

    public boolean sismember(final String key,final String value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public long ssize(final String key){
        return redisTemplate.opsForSet().size(key);
    }

    public Map hgetAll(final String key){
        return redisTemplate.opsForHash().entries(key);
    }

    public Object hget(final String key,final String field){
        return redisTemplate.opsForHash().get(key,field);
    }

    public void hputAll(final String key,final Map<String,String> map){
        redisTemplate.opsForHash().putAll(key,map);
    }

    public void hput(final String key,final String field,final String value){
        redisTemplate.opsForHash().put(key,field,value);
    }

    public boolean hputIfAbsent(final String key,final String field,final String value){
        return redisTemplate.opsForHash().putIfAbsent(key,field,value);
    }

    public void hdel(final String key,final String... fields){
        redisTemplate.opsForHash().delete(key,fields);
    }

    public BoundHashOperations getHashOps(String key){
        return redisTemplate.boundHashOps(key);
    }

    public void publish(String channel,String value){
        redisTemplate.convertAndSend(channel, value);
    }




    /**
     * 设置对象
     * @param key
     * @param obj
     */
    public  void setObject(String key ,Object obj) {
        obj = obj == null ? new Object():obj;
        this.set(key, JSONUtil2.objectToJson(obj));
    }

    /**
     * 获取对象
     * @param key
     * @return Object
     */
    public  <T> T getObject(String key,Class<T> clazz){
        String str = this.get(key);
        return JSONUtil2.fromJson(str,clazz);
    }
    public  Object getObject(String key){
        String str = this.get(key);
        return JSONUtil2.fromJson(str,Object.class);
    }
    /**
     * 设置List集合
     * @param key
     * @param list
     */
    public  void setList(String key ,List<?> list){
        String value = JSONUtil2.objectToJson(list);
        this.set(key,value);
    }

    /**
     * 获取List集合
     * @param key
     * @return
     */
    public  <T> List<T> getList(String key,Class<T> clazz){
        String list = this.get(key);
        return JSONUtil2.jsonToList(list,clazz);
    }
    public  List getList(String key){
        String list = this.get(key);
        return JSONUtil2.jsonToList(list,Object.class);
    }
}

