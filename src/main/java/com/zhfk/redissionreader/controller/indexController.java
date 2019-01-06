package com.zhfk.redissionreader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class indexController {

    @RequestMapping({"/", "/index"})
    public String index(){
        return "index";
    }

}
