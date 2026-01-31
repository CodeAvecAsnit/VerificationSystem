package com.techdgnep.login.service.extras.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MailPublisherClass {

    private final String kafkaTopic = "mail_events";
    private final KafkaTemplate<String,String> kafkaTemplate;

    public MailPublisherClass(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEmail(String email){
        kafkaTemplate.send(kafkaTopic,email);
    }
}
