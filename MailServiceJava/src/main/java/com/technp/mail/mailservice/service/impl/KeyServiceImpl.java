package com.technp.mail.mailservice.service.impl;

import com.technp.mail.mailservice.exception.custom.CryptoOperationException;
import com.technp.mail.mailservice.service.KeyService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class KeyServiceImpl implements KeyService {

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)
                    ((Character.digit(hex.charAt(i), 16) << 4)
                            + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }


    @Override
    public boolean comparePassword(String password, String hashedPassword){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] arr = md.digest(password.getBytes(StandardCharsets.UTF_8));
            byte[] arr2 = hexToBytes(hashedPassword);
            return MessageDigest.isEqual(arr,arr2);
        }catch (NoSuchAlgorithmException ex){
            throw new CryptoOperationException("Internal Error");
        }
    }
}
