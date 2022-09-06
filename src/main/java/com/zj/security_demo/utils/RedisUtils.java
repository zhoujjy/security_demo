package com.zj.security_demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //JSON工具
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 保存键值对
     * @param key
     * @param value
     * @param <T>
     * @throws JsonProcessingException
     */
   public<T>  void setObject(String key,T value) throws JsonProcessingException {

       String json = mapper.writeValueAsString(value);

       stringRedisTemplate.opsForValue().set(key,json);
   }

    /**
     * 保存键值对并设置过期时间
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @param <T>
     * @throws JsonProcessingException
     */
    public<T>  void setObject(String key, T value, Long timeout, TimeUnit timeUnit) {

        String json = null;
        try {
            json = mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        stringRedisTemplate.opsForValue().set(key,json,timeout,timeUnit);
    }

    public void setTime(String key ,Long timeout,TimeUnit timeUnit){
        stringRedisTemplate.expire(key,timeout,timeUnit);
    }

    /**
     * 获取指定键的值
     * @param key
     * @param clazz
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public<T> T getObject(String key,Class<T> clazz) throws JsonProcessingException {
        String jsonValue = stringRedisTemplate.opsForValue().get(key);
        return mapper.readValue(jsonValue, clazz);
    }

    /**
     * 获取redis内所有值
     * @return
     */
    public <T> List<T> getAllData(Class<T> clazz){
        Set<String> keys = stringRedisTemplate.keys("*");
        List<String> lists = stringRedisTemplate.opsForValue().multiGet(keys);
        List<T> tList = new ArrayList<>();
        try {
            for(String list:lists){
                tList.add(mapper.readValue(list,clazz));
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
        return tList;
    }

    /**
     * 删除缓存
     * @param key
     */
    public void removeValue(String key){
        stringRedisTemplate.delete(key);
    }
}
