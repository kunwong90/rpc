package com.rpc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class HelloController {

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }
}
