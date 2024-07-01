package com.unipi.cloudpatterns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Redirect all non-static requests to the index.html
        return "forward:/index.html";
    }
}

