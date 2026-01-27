package com.techdgnep.login.service.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdgnep.login.data.dto.VerificationDTO;
import com.techdgnep.login.service.kafka.KafkaCodePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KafkaCodePublisherImpl extends Thread implements KafkaCodePublisher {

    private final Logger logger = LoggerFactory.getLogger(KafkaCodePublisherImpl.class);
    private final String kafkaTopic = "otp_mail";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaCodePublisherImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public void publishEmail(VerificationDTO verificationDTO) throws JsonProcessingException {
        String verificationJSON = objectMapper.writeValueAsString(verificationDTO);
        kafkaTemplate.send(kafkaTopic,verificationJSON);
    }

    @Override
    public void run() {
        super.run();
    }
}
