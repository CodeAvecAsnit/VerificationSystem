package com.autowhouse.mailservice.service.mail;

import com.autowhouse.mailservice.dto.VerificationDTO;
import org.springframework.stereotype.Service;

@Service
public interface MailService {

    void sendEmail(VerificationDTO verificationDTO);
}
