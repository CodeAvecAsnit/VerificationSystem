package com.techdgnep.login.Cotroller;

import com.techdgnep.login.Service.EmailPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/mail/publish")
public class EmailController{

    private final EmailPublisherService emailPublisherService;

    @Autowired
    public EmailController(@Qualifier("emailPublisherServiceImpl") EmailPublisherService emailPublisherService) {
        this.emailPublisherService = emailPublisherService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void send(@RequestBody Map emailMap){
        emailPublisherService.publishEmail((String)emailMap.get("email"));
    }
}