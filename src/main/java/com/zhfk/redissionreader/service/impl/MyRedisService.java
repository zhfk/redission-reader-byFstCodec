package com.zhfk.redissionreader.service.impl;

import com.zhfk.redissionreader.configuration.RedissionBaseConfig;
import com.zhfk.redissionreader.module.MyRedisClient;
import com.zhfk.redissionreader.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
public class MyRedisService implements RedisService {

    @Autowired
    RedissionBaseConfig redissionBaseConfig;

    @Override
    public List<Integer> getAllDBs(MyRedisClient redisClient) {
        List<Integer> dbs = Arrays.asList(1,2,3,4);
        return dbs;
    }

    @Override
    public RedissonClient connect(MyRedisClient redisClient) {
        RedissonClient client = null;
        try {
            client = redissionBaseConfig
                    .getRedissonClient(
                            String.format("redis://%s:%d", redisClient.getHost(), redisClient.getPort()),
                            StringUtils.isEmpty(redisClient.getPass()) ? null : redisClient.getPass(),
                            redisClient.getDb());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return client;

    }

    public List<String> getAllKeys() {
        return null;
    }

    @Override
    public List<Object> getObjectByKey(String key) {
        return null;
    }
}
