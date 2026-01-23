package com.techdgnep.login.Service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailPublisherServiceImpl implements EmailPublisherService{

    private final String kafkaTopic = "mail_event";
    private KafkaTemplate<>

    @Override
    public void publishEmail(String email){

    }

}
