package com.zhfk.redissionreader.controller;

import com.zhfk.redissionreader.module.MyRedisClient;
import com.zhfk.redissionreader.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/redission")
public class redissionController {

    RedissonClient redissonClient = null;

    @Autowired
    RedisService redisService;

    @RequestMapping("/client")
    public String index(Model model) {
        if(redissonClient == null || redissonClient.isShutdown()){
            model.addAttribute("myRedisClient", new MyRedisClient());
        }
        return "redission_connect";
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String save(Model model){
       RSet<Object> sc = redissonClient.getSet("zhfk:test:set");
       sc.clear();
       sc.addAll(Arrays.asList("S1","S2","S4","S6","S29","S13"));

       RList<Object> lc = redissonClient.getList("zhfk:test:list");
       lc.clear();
       lc.addAll(Arrays.asList("S1","S2","S4","S6","S29","S13"));

       RMap<String, Object> mc = redissonClient.getMap("zhfk:test:map");
       mc.clear();
       Map<String, Integer> m1 = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            m1.put("k"+i, i);
        }
       mc.putAll(m1);

       log.info("save objs succeed!");
        model.addAttribute("connected",true);
       return "redission_query";
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connect(@ModelAttribute(value = "myRedisClient") MyRedisClient redisClient, Model model) {
        if(redissonClient == null || redissonClient.isShutdown()){
            redissonClient = redisService.connect(redisClient);
        }
        if (null != redissonClient) {
            //连接成功
            model.addAttribute("msg",
                    String.format("Congratulation connect %s:%d db:%d succeed !",
                            redisClient.getHost(),
                            redisClient.getPort(),
                            redisClient.getDb()));
            model.addAttribute("connected",true);
        } else {
            //连接成功
            model.addAttribute("msg",
                    String.format("connect %s:%d db:%d failed !",
                            redisClient.getHost(),
                            redisClient.getPort(),
                            redisClient.getDb()));
            model.addAttribute("connected",false);
        }
        return "redission_query";
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(String key, Model model){

        RType keyType = redissonClient.getKeys().getType(key);
        model.addAttribute("key", key);
        model.addAttribute("connected",true);

        String type = "key not found!";
        if(keyType == null) {
            model.addAttribute("valueType", type);
            return "redission_query";
        }
        switch (keyType){
            case SET:
                model.addAttribute("values", redissonClient.getSet(key).readAll());
                type = "SET";
                break;
            case LIST:
                model.addAttribute("values", redissonClient.getList(key).readAll());
                type = "LIST";
                break;
            case MAP:
                model.addAttribute("values",  redissonClient.getMap(key).readAllEntrySet());
                type = "MAP";
                break;
            case ZSET:
                model.addAttribute("values",  redissonClient.getSortedSet(key).readAll());
                type = "ZSET";
                break;
            case OBJECT:
                log.info("unsupport object");
                type = "unknow";
                break;
                default:
                    log.info("unsupport object");
                    type = "unknow";
        }
        model.addAttribute("querySucceed",true);
        model.addAttribute("valueType", type);
        return "redission_query";
    }

    @RequestMapping(value = "/disconnect",method = RequestMethod.POST)
    public String disconnect(Model model){
        redissonClient.shutdown();
        model.addAttribute("shutdown",true);
        log.info("rediss disconnect succeed!");
        return "redission_connect";
    }
}
