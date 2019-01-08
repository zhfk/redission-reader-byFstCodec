package com.zhfk.redissionreader.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhfk.redissionreader.module.MyKeyValue;
import com.zhfk.redissionreader.module.MyRedisClient;
import com.zhfk.redissionreader.module.ResponseData;
import com.zhfk.redissionreader.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/redission")
public class redissionController {

    RedissonClient redissonClient = null;

    @Autowired
    RedisService redisService;

    @RequestMapping("/client")
    public String client() {
        return "redission_client";
    }

//    @RequestMapping(value = "/save", method = RequestMethod.GET)
//    public String save(Model model){
//       RSet<Object> sc = redissonClient.getSet("zhfk:test:set");
//       sc.clear();
//       sc.addAll(Arrays.asList("S1","S2","S4","S6","S29","S13"));
//
//       RList<Object> lc = redissonClient.getList("zhfk:test:list");
//       lc.clear();
//       lc.addAll(Arrays.asList("S1","S2","S4","S6","S29","S13"));
//
//       RMap<String, Object> mc = redissonClient.getMap("zhfk:test:map");
//       mc.clear();
//       Map<String, Integer> m1 = new HashMap<>();
//        for (int i = 0; i < 18; i++) {
//            m1.put("k"+i, i);
//        }
//       mc.putAll(m1);
//
//        RScoredSortedSet scoreSet = redissonClient.getScoredSortedSet("zhfk:test:scoreset");
//        scoreSet.clear();
//        Random random = new Random(100);
//        for (int i = 0; i < 36 ; i++) {
//            scoreSet.add(random.nextDouble(), new User(i,"user-"+i, "BJ", 20 + i%10));
//        }
//
//        RBucket<String> bucket = redissonClient.getBucket("zhfk:test:string");
//        bucket.set("my name is zhfk!");
//
//       log.info("save objs succeed!");
//        model.addAttribute("connected",true);
//       return "redission_query";
//    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData connect(@RequestBody MyRedisClient redisClient) {
        if(redissonClient == null || redissonClient.isShutdown()){
            redissonClient = redisService.connect(redisClient);
        }
        if (null != redissonClient && !redissonClient.isShutdown()) {
            log.info("redission connect succeed");
            return new ResponseData<>(200, "connect succeed", 0, redissonClient.toString());
        } else {
            return new ResponseData<>(500, "connect failed", 0, null);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(@RequestBody JSONObject param){
        String key = param.getString("key");
        RType keyType = redissonClient.getKeys().getType(key);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        String type = "key not found!";
        if (keyType == null) {
            jsonObject.put("type", type);
            return new ResponseData<>(200, type, 0, jsonObject);
        } else {
            jsonObject.put("type", keyType.name());
            return new ResponseData<>(200, "succeed", 0, jsonObject);
        }
    }

    @RequestMapping(value = "/data")
    @ResponseBody
    public ResponseData getData(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit){
        JSONObject jsonObject = new JSONObject();
        switch (type){
            case "SET":
                jsonObject.put("values", redissonClient.getSet(key).readAll());
                break;
            case "LIST":
                jsonObject.put("values", redissonClient.getList(key).readAll());
                break;
            case "MAP":
                jsonObject.put("values", redissonClient.getMap(key).readAllEntrySet());
                break;
            case "ZSET":
                jsonObject.put("values", redissonClient.getSortedSet(key).readAll());
                break;
            case "OBJECT":
                jsonObject.put("values", Collections.singletonList(redissonClient.getBucket(key).get()));
                break;
            default:
                jsonObject.put("values",Collections.emptyList());
                log.info("unsupport object");
        }
        Collection<Object> values = (Collection<Object>) jsonObject.get("values");
        List<MyKeyValue> data = values.stream().map(i -> new MyKeyValue("key", i.toString())).collect(Collectors.toList());

        Integer beforeNum = (page - 1) * limit;
        int count = data.size();

        return new ResponseData<>(0, "succeed", data.size(), data.subList(beforeNum, Integer.min(beforeNum+limit, count)));
    }

    @RequestMapping(value = "/disconnect",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData disconnect(){
        try{
            redissonClient.shutdown();
            log.info("redission disconnect succeed");
            return new ResponseData<>(200, "disconnect succeed", 0, null);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            redissonClient = null;
        }
        return new ResponseData<>(500, "disconnect error", 0, null);
    }
}
