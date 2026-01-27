package com.techdgnep.login.data.dto;

import lombok.Data;

@Data
public class VerificationDTO {
    private String email;
    private int verificationCode;
}
