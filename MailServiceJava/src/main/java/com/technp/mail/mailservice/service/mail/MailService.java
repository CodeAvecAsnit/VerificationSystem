package com.technp.mail.mailservice.service.mail;

import com.technp.mail.mailservice.dto.VerificationDTO;
import org.springframework.stereotype.Service;

@Service
public interface MailService {

    void sendEmail(VerificationDTO verificationDTO);
}
