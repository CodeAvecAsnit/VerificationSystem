package com.autowhouse.mailservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.autowhouse.mailservice.dto.VerificationDTO;
import com.autowhouse.mailservice.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaCodeAndMailService {

    private final ObjectMapper objectMapper;
    private final MailService mailService;

    public KafkaCodeAndMailService(ObjectMapper objectMapper, MailService mailService) {
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "otp_mail")
    public void listen(String verificationData) throws JsonProcessingException {
        VerificationDTO verificationDTO = objectMapper.readValue(verificationData,VerificationDTO.class);
        log.info("Initiating mail for {}",verificationDTO.getEmail());
        mailService.sendEmail(verificationDTO);
    }
}
