package com.techdgnep.login.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailPublisherServiceImpl implements EmailPublisherService{

    private final Logger log = LoggerFactory.getLogger(EmailPublisherServiceImpl.class);
    private final String kafkaTopic = "mail_event";
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
