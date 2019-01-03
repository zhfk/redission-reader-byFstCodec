package com.zhfk.redissionreader.controller;

import com.zhfk.redissionreader.module.MyRedisClient;
import com.zhfk.redissionreader.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RKeys;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Log4j2
@Controller
public class IndexController {

    RedissonClient redissonClient = null;

    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        if(redissonClient == null || redissonClient.isShutdown()){
            model.addAttribute("myRedisClient", new MyRedisClient());
        }
        return "connect";
    }

//    @RequestMapping(value = "/save", method = RequestMethod.GET)
//    public void save(){
//       RSet<Object> sc = redissonClient.getSet("zhfk:test:set");
//       sc.addAll(Arrays.asList("S1","S2","S4","S6","S29","S13"));
//
//       RList<Object> lc = redissonClient.getList("zhfk:test:list");
//       lc.addAll(Arrays.asList("S1","S2","S4","S6","S29","S13"));
//
//       RMap<String, Object> mc = redissonClient.getMap("zhfk:test:map");
//       Map<String, Integer> m1 = new HashMap<>();
//       m1.put("k1", 1);
//       m1.put("k2", 2);
//       m1.put("k30", 30);
//       m1.put("k300", 300);
//       mc.putAll(m1);
//
//       log.info("save objs succeed!");
//    }

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
            RKeys keys = redissonClient.getKeys();
            model.addAttribute("redisKeys", keys.getKeys());
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
        return "query";
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(String key, Model model){

        RType keyType = redissonClient.getKeys().getType(key);
        model.addAttribute("key", key);
        model.addAttribute("connected",true);

        String type = "key not found!";
        if(keyType == null) {
            model.addAttribute("valueType", type);
            return "query";
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
        return "query";
    }

//    @RequestMapping(value = "/keys", method = RequestMethod.GET)
//    public String searchKeys(String keyPattern,Model model){
//        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(keyPattern);
//        model.addAttribute("keys",keys);
//        return "keys";
//    }

    @RequestMapping(value = "/disconnect",method = RequestMethod.POST)
    public String disconnect(Model model){
        redissonClient.shutdown();
        model.addAttribute("shutdown",true);
        log.info("rediss disconnect succeed!");
        return "connect";
    }
}
