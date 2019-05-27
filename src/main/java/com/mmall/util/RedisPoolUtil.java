package com.mmall.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolUtil {

    public static JedisPool pool;
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
    }
}
