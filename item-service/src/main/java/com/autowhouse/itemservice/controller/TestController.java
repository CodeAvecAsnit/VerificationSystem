package com.autowhouse.itemservice.controller;

import com.autowhouse.itemservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(@Qualifier("testServiceImpl") TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/here")
    public ResponseEntity<String> test(){
        return (testService.test())? ResponseEntity.ok("Success") :
                ResponseEntity.badRequest().build();
    }

    @GetMapping("/then")
    public String here(){
        String str = "The method has been executed";
        System.out.println(str);
        return str;
    }
}
