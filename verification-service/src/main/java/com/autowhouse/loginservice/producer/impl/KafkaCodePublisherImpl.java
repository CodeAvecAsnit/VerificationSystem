package com.autowhouse.loginservice.producer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import com.autowhouse.loginservice.service.kafka.KafkaCodePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author : Asnit Bakhati
 * @Date : 10th Feb, 2026
 */
@Service
@Slf4j
public class KafkaCodePublisherImpl implements KafkaCodePublisher {

    @Value("${application.kafka.topic.otp}")
    private String kafkaTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaCodePublisherImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishEmail(VerificationDTO verificationDTO) {
        try {
            String verificationJSON = objectMapper.writeValueAsString(verificationDTO);
            String messageKey = verificationDTO.getEmail();

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(kafkaTopic, messageKey, verificationJSON);

            future.whenComplete((result, ex) -> {
                if (ex != null)
                    log.error("Failed to publish verification email for {}: {}",
                            verificationDTO.getEmail(), ex.getMessage(), ex);
                 else
                    log.info("Successfully published verification email for {} to partition {} with offset {}",
                            verificationDTO.getEmail(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize VerificationDTO for {}: {}",
                    verificationDTO.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to publish verification email", e);
        }
    }
}
