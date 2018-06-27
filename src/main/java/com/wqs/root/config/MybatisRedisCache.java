package com.wqs.root.config;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用Redis来做Mybatis的二级缓存
 * 实现Mybatis的Cache接口
 */
public class MybatisRedisCache implements Cache {
    {
        System.out.println("进入到缓存中了!!!");
    }
    private static final Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);
    //读写锁
    private final ReadWriteLock readWriteLock=new ReentrantReadWriteLock(true);
    //redis 类
    private RedisTemplate<String,Object> redisTemplate=SpringContextHolder.getBean("redisTemplate");
    private String id;
    public MybatisRedisCache(final String id){
        if(id==null){
            throw new IllegalArgumentException("redis Cache require id");
        }
        this.id=id;
    }
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        if(value!=null){
            redisTemplate.opsForValue().set(key.toString(),value,2, TimeUnit.DAYS);
        }
    }

    @Override
    public Object getObject(Object key) {
        if(key!=null){
            return redisTemplate.opsForValue().get(key.toString());
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        try {
            if(key!=null){
                redisTemplate.delete(key.toString());
            }
        }catch (Exception e){
            logger.error("redis ");
        }
        return null;
    }

    @Override
    public void clear() {
        logger.debug("清空缓存");
        try {
           Set<String> keys= redisTemplate.keys("*:" + this.id + "*");
           if(keys!=null){
                redisTemplate.delete(keys);
           }
        }catch (Exception e){

        }
    }

    @Override
    public int getSize() {
        Long  size=redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.dbSize();
            }
        });
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}
