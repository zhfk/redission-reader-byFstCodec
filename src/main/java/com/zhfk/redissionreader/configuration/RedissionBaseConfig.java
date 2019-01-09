package com.zhfk.redissionreader.configuration;

import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

@Log4j2
@Configuration
public class RedissionBaseConfig {

    public Config getDefaultConfig(){
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Config config = null;
        try {
            Resource[] resources = resolver.getResources("classpath*:redis-config-base.json");
            config = Config.fromJSON(resources[0].getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return config;
    }

    public RedissonClient getRedissonClient(String address, String pass, int database){

        Config config = getDefaultConfig();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(pass)
                .setDatabase(database);

        return Redisson.create(config);
    }

}