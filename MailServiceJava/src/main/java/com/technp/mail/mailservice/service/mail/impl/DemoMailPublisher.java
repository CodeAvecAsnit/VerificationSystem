package com.technp.mail.mailservice.service.mail.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoMailPublisher {

    @KafkaListener(topics = "mail_events")
    public void listen(String email){
        int x = 1;
        int y = 2;
        int sum = x+y;
        log.info("{}",sum);
        log.debug("{} has now reached mail server and now sending the mail server.",email);
    }
}
