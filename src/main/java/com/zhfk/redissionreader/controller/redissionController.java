package com.zhfk.redissionreader.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhfk.redissionreader.constant.Codecs;
import com.zhfk.redissionreader.module.MyKeyValue;
import com.zhfk.redissionreader.module.MyRedisClient;
import com.zhfk.redissionreader.module.ResponseData;
import com.zhfk.redissionreader.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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


    @RequestMapping(value = "/codecs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getCodecs(){
        List<String> codecs = Arrays.stream(Codecs.values()).map(e -> e.name).collect(Collectors.toList());
        return new ResponseData<>(200, "succeed", 0, codecs);
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

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData connect(@RequestBody JSONObject object) {
        return new ResponseData<>(200, "succeed", 0, object);
    }

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

    @RequestMapping(value = "/data")
    @ResponseBody
    public ResponseData getData(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit){
        JSONObject jsonObject = new JSONObject();
        int count = 0;
        int index = 0;
        List<MyKeyValue> data = new ArrayList<>();
        switch (type){
            case "RSet":
                RSet<Object> rSet = redissonClient.getSet(key);
                count = rSet.size();
                Set<Object> rSetObjs = rSet.readAll();
                for (Object o:rSetObjs){
                    data.add(new MyKeyValue(index++,o.toString(),null));
                }
                break;
            case "RList":
                RList<Object> rList = redissonClient.getList(key);
                count = rList.size();
                List<Object> rListObjs = rList.readAll();
                for (Object o:rListObjs){
                    data.add(new MyKeyValue(index++,o.toString(),null));
                }
                break;
            case "RMap":
                RMap<Object, Object> rMap = redissonClient.getMap(key);
                count = rMap.size();
                Set<Map.Entry<Object, Object>> enties = rMap.readAllEntrySet();
                for (Map.Entry e: enties){
                    data.add(new MyKeyValue(index++, e.toString(), null));
                }
                break;
            case "RSortedSet":
                RSortedSet<Object> rSortedSet = redissonClient.getSortedSet(key);
                count = rSortedSet.size();
                Collection<Object> rSortedSetObjs = rSortedSet.readAll();

                for (Object o:rSortedSetObjs){
                    data.add(new MyKeyValue(index++, o.toString(), null));
                }
                break;
            case "RScoredSortedSet":
                RScoredSortedSet<Object> rScoredSortedSet = redissonClient.getScoredSortedSet(key);
                count = rScoredSortedSet.size();
                Object[] rScoredSortedSetObjs = rScoredSortedSet.readAll().toArray();

                for (int i = 0; i < rScoredSortedSetObjs.length; i++) {
                    data.add(
                            new MyKeyValue(i,
                                    rScoredSortedSetObjs[i].toString(),
                                    rScoredSortedSet.getScore(rScoredSortedSetObjs[i])
                            )
                    );
                }
                break;
            default:
                log.info("type for "+type+" not support yet!");
        }
        Integer beforeNum = (page - 1) * limit;
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
