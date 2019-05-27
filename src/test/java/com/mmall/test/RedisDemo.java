package com.mmall.test;

import com.mmall.util.RedisPoolUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class RedisDemo {

    public static void main(String[] args) {


        Jedis jedis = RedisPoolUtil.getJedis();

        System.out.println(jedis.ping());

        RedisPoolUtil.close(jedis);
    }

    /**
     * 测试字符串
     */
    @Test
    public void t1(){
        String host = "192.168.1.197";
        int port = 6379;
        Jedis jedis = new Jedis(host,port);
//        jedis.auth("");
        System.out.println(jedis.ping());
        jedis.set("strName","字符串名称");

        System.out.println(jedis.get("strName"));
        jedis.close();
    }
    /**
     * 为了减轻数据库的访问压力
     * 需求：判断某KEY是否存在，若存在，则直接查询
     * 若不存在，查询数据库，将查到的数据存入redis
     */
    @Test
    public void t2(){
        String host = "192.168.1.197";
        int port = 6379;
        Jedis jedis = new Jedis(host,port);
//        jedis.auth("");
        String key = "applicationName";
        if (jedis.exists(key)){
            String result = jedis.get(key);
            System.out.println("Redis 中查询得到："+result);
        }else {
            //在数据库查询
            String result = "微信值";
            jedis.set(key,result);
            System.out.println("Mysql中查询得到:"+ jedis.get(key));
        }
        jedis.close();
    }


    /**
     * Jedis 完成对Hash类型操作
     * 需求：Hash 存储一个对象
     * 判断redis是否存在该对象
     * 若存在，直接返回，否则查询数据库，写入
     */

    @Test
    public void t3(){
        Jedis jedis = RedisPoolUtil.getJedis();
        String key = "user:1";
        if( jedis.exists(key)){
            Map<String, String> map = jedis.hgetAll(key);
            System.out.println("----redis 查询结果");
            System.out.println(map.get("id")+ map.get("name"));
        }else {
            //查询数据库
            String id = "1";
            String name = "yourname";

            jedis.hset(key,"id",id);
            jedis.hset(key,"name",name);
            jedis.hset(key,"age","22");
            jedis.hset(key,"ramake","新同学");
            System.out.println("数据库查询结果:"+id+" " + name);

        }
    }

}
