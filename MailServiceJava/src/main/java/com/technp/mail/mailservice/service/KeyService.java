package com.technp.mail.mailservice.service;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public interface KeyService {
    boolean comparePassword(String password,String hashedPassword);
}
