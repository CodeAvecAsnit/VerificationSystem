package com.techdgnep.login.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GetCode {
    private final RestTemplate restTemplate;

    public GetCode(){
        this.restTemplate = new RestTemplate();
    }

    public Integer GetMailCode(String email) throws Exception{
        String url = "http://localhost:5201/api/mail/send?email="+email;
        return restTemplate.getForObject(url,Integer.class);
    }
}
