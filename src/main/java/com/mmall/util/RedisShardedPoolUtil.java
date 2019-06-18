package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisShardedPoolUtil {

    //extime单位是秒
    public static String setEx(String key, String value, int exTime){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        }catch (Exception e){
            log.error("setex key:{} value:{] error",key, value, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;

    }

    /**
     * 设置key的有效期，单位为秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        }catch (Exception e){
            log.error("expire key:{} error",key,  e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;

    }


    public static String set(String key, String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        }catch (Exception e){
            log.error("set key:{} value:{] error",key, value, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;

    }

    public static Long setnx(String key, String value){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
        }catch (Exception e){
            log.error("setnx key:{} value:{] error",key, value, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;

    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} error",key, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }


    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{} error",key, e);
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }


    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        RedisShardedPoolUtil.set("keytest","value");

        String value = RedisShardedPoolUtil.get("keytest");

        RedisShardedPoolUtil.setEx("keyex","valueex", 60*10);

        RedisShardedPoolUtil.expire("keytest",60*20);

        RedisShardedPoolUtil.del("keytest");

        System.out.println("end");
    }
}
