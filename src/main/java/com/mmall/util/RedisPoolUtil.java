package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class RedisPoolUtil {

    /*public static JedisPool pool;
    static {
        //1.创建连接池 redis pool 基本配置信息
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(1);
        poolConfig.setMaxTotal(5);
        //其他配置

        //连接池
        String host = "192.168.1.197";
        int port = 6379;
        pool = new JedisPool(poolConfig,host,port);

    }
    public static Jedis getJedis(){
        Jedis jedis = pool.getResource();
//        jedis.auth();
        return jedis;
    }


    public static void close(Jedis jedis){
        jedis.close();
    }*/

    //extime单位是秒
    public static String setEx(String key, String value, int exTime){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        }catch (Exception e){
            log.error("setex key:{} value:{] error",key, value, e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;

    }

    /**
     * 设置key的有效期，单位为秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        }catch (Exception e){
            log.error("expire key:{} error",key,  e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;

    }


    public static String set(String key, String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        }catch (Exception e){
            log.error("set key:{} value:{] error",key, value, e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;

    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} error",key, e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }


    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{} error",key, e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }


    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisPoolUtil.set("keytest","value");

        String value = RedisPoolUtil.get("keytest");

        RedisPoolUtil.setEx("keyex","valueex", 60*10);

        RedisPoolUtil.expire("keytest",60*20);

        RedisPoolUtil.del("keytest");

        System.out.println("end");
    }
}
