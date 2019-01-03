package com.zhfk.redissionreader.service;

import com.zhfk.redissionreader.module.MyRedisClient;
import org.redisson.api.RedissonClient;

import java.util.List;


public interface RedisService {
   public List<Integer> getAllDBs(MyRedisClient redisClient);
   public RedissonClient connect(MyRedisClient redisClient);
   public List<String> getAllKeys();
   public List<Object> getObjectByKey(String key);
}
