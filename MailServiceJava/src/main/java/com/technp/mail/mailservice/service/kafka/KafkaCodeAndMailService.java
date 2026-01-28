package com.technp.mail.mailservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technp.mail.mailservice.dto.VerificationDTO;
import com.technp.mail.mailservice.service.mail.MailService;
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

    @KafkaListener(topics = "ma")
    public void listen(String verificationData) throws JsonProcessingException {
        VerificationDTO verificationDTO = objectMapper.readValue(verificationData,VerificationDTO.class);
        mailService.sendEmail(verificationDTO);
    }
}
