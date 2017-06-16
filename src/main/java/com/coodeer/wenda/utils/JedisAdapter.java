package com.coodeer.wenda.utils;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by common on 2017/6/15.
 */
@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = Logger.getLogger(InitializingBean.class);

    private JedisPool jedisPool;

    public long sadd(String name, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(name, value);

        }catch (Exception e){
            logger.error("发生异常： " + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String name, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.srem(name, value);

        }catch (Exception e){
            logger.error("发生异常： " + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String name){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.scard(name);

        }catch (Exception e){
            logger.error("发生异常： " + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String name, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(name, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String name){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, name);
        }catch (Exception e){
            logger.error("发生异常： " + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String name, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.lpush(name, value);
        }catch (Exception e){
            logger.error("发生异常： " + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/0");
    }
}
