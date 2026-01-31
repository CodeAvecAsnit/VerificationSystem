package com.techdgnep.login.service.extras.impl;

import com.techdgnep.login.service.extras.EmailPublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailPublisherServiceImpl implements EmailPublisherService {

    private final Logger log = LoggerFactory.getLogger(EmailPublisherServiceImpl.class);
    private final String kafkaTopic = "mail_events";
    private final KafkaTemplate<String,String> kafka;

    @Autowired
    public EmailPublisherServiceImpl(KafkaTemplate<String, String> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void publishEmail(String email){
        kafka.send(kafkaTopic,email);
        log.debug("{} is sending email",email);
    }
}
