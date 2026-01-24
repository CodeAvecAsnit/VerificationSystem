package com.technp.mail.mailservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DemoMailPublisher {

    Logger logger = LoggerFactory.getLogger(DemoMailPublisher.class);

    @KafkaListener(topics = "mail_events")
    public void listen(String email){
        int x = 1;
        int y = 2;
        int sum = x+y;
        logger.info("{}",sum);
        logger.debug("{} has now reached mail server and now sending the mail server.",email);
    }
}
