package com.raf.cinemauserservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class PagesController {

    @RequestMapping
    public String getStartPage(){
        return "index.html";
    }

}
