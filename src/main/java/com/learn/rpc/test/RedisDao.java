package com.learn.rpc.test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(RedisDao.class);

    private JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<User> schema = RuntimeSchema.createFrom(User.class);

    public User saveAndGetUser(User user) {
        try {
            String key = "user:" + user.getId();
            byte[] bytes = ProtobufIOUtil.toByteArray(user, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

            Jedis jedis = jedisPool.getResource();
            int timeOut = 600;
            jedis.setex(key.getBytes(), timeOut, bytes);


            byte[] bytes1 = jedis.get(key.getBytes());

            User user1 = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes1, user1, schema);
            return user1;
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }
}
