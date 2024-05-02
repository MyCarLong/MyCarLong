package com.mycarlong.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.TimeZone;

@Controller
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);
    @PostMapping
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @RequestMapping({"/","/main"})
    public String mainAPI(){
        logger.info(String.valueOf(System.currentTimeMillis()));
        logger.info(String.valueOf(System.currentTimeMillis() + 1000 * 60 * 60L));

        return "index";
    }

}
