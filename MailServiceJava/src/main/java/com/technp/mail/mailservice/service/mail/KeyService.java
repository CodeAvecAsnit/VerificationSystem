package com.technp.mail.mailservice.service.mail;

import org.springframework.stereotype.Service;

@Service
public interface KeyService {
    boolean comparePassword(String password,String hashedPassword);
}
