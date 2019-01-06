package com.zhfk.redissionreader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layui")
public class layuiController {

    @RequestMapping("/first")
    public String first(){
        return "layui_first";
    }

}
