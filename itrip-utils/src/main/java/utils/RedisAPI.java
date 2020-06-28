package utils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisAPI {
    public JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    //get
    public String get(String key){
        Jedis jedis=jedisPool.getResource();
        String value=jedis.get(key);
        jedisPool.returnResource(jedis);
        return value;
    }
    //set
    public String set(String key,String value){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.set(key,value);
        jedisPool.returnResource(jedis);
        return result;
    }
    public String set(String key,String value,int seconds){
        Jedis jedis=jedisPool.getResource();
        String result=jedis.setex(key,seconds,value);
        jedisPool.returnResource(jedis);
        return result;
    }
    //exists
    public boolean exists(String key){
        Jedis jedis=jedisPool.getResource();
        boolean result=jedis.exists(key);
        jedisPool.returnResource(jedis);
        return result;
    }
    //ttl
    public long ttl(String key)
    {
        Jedis jedis=jedisPool.getResource();
        long result=jedis.ttl(key);
        jedisPool.returnResource(jedis);
        return result;
    }
    //del
    public long del(String key){
        Jedis jedis=jedisPool.getResource();
        long result=jedis.del(key);
        jedisPool.returnResource(jedis);
        return result;
    }
}
