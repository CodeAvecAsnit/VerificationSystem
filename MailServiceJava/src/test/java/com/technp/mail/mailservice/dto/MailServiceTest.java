package com.technp.mail.mailservice.dto;

import com.technp.mail.mailservice.service.impl.KeyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @InjectMocks
    private KeyServiceImpl keyService;

    @Test
    void checkMatchCondition() throws NoSuchAlgorithmException {
        String pass1 = "randomPass1";
        String hashedPass = hash(pass1);
        assertTrue(keyService.comparePassword(pass1,hashedPass),"Passwords should match if they are same");
    }

    @Test
    void checkNotMatchCondition() throws NoSuchAlgorithmException {
        String pass1 = "randomPass1";
        String secondHashedPass = hash("completelyanotherPass@12");
        assertFalse(keyService.comparePassword(pass1,secondHashedPass),"Passwords should match if they are same");
    }

    public String hash(String message) {
        try{
//            String message = "IWant2Make$$Money$$67@9861";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] arr = md.digest(message.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : arr) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
