package com.zhfk.redissionreader.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhfk.redissionreader.module.ResponseData;
import com.zhfk.redissionreader.module.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/{id}")
    public String getUser(@PathVariable int id, Model model) {
        User user = new User(id, "pepstack-" + id, "Shanghai, China", 20 + id);
        model.addAttribute("user", user);
        return "user_detail";
    }

    @RequestMapping("/list")
    public String listUser(Model model){
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            User dto = new User();

            dto.setId(i);
            dto.setUsername("pepstack-" + i);
            dto.setAddress("Shanghai, China");
            dto.setAge(20 + i);

            userList.add(dto);
        }

        model.addAttribute("users", userList);

        return "user_list";
    }

    @RequestMapping("/list2")
    @ResponseBody
    public ResponseData listUser2(
            @PathParam(value = "page") Integer page,
            @PathParam(value = "limit") Integer limit){

        List<User> userList = new ArrayList<>();

        Integer beforeNum = (page - 1) * limit;

        int count = 95;

        for (int i = beforeNum; i < beforeNum+limit && i< count; i++) {
            User user = new User();

            user.setId(i);
            user.setUsername("pepstack-" + i);
            user.setAddress("Shanghai, China");
            user.setAge(20 + i%10);
            userList.add(user);
        }
        return new ResponseData<>(0,"", count, userList);
    }

}
