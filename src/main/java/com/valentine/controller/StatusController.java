package com.valentine.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class StatusController {


    @Value("${api-host}")
    String apiHost;

    @Value("${open-host}")
    private String openHost;

    @RequestMapping(value = "/apiHost")
    public String apiHost(){
        return apiHost;
    }

    @GetMapping("/openHost")
    public String openHost() {
        return openHost;
    }

}
