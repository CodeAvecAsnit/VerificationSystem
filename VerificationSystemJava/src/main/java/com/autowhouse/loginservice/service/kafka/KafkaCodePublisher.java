package com.autowhouse.loginservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import org.springframework.scheduling.annotation.Async;

public interface KafkaCodePublisher {
    @Async
    void publishEmail(VerificationDTO verificationDTO) throws JsonProcessingException;
}
