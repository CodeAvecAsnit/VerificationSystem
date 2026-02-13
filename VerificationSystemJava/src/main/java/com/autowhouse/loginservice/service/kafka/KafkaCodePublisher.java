package com.autowhouse.loginservice.service.kafka;

import com.autowhouse.loginservice.data.dto.VerificationDTO;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public interface KafkaCodePublisher {
    void publishEmail(VerificationDTO verificationDTO);
}
