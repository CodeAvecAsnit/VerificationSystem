package com.technp.mail.mailservice.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    Integer SendEmail(String email);
}
