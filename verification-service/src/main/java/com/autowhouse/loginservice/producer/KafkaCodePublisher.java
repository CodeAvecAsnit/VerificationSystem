package com.autowhouse.loginservice.producer;

import com.autowhouse.loginservice.data.dto.VerificationDTO;

/**
 * @author : Asnit Bakhati
 * @Date : 12th Feb, 2026
 */
public interface KafkaCodePublisher {
    void publishEmail(VerificationDTO verificationDTO);
}
