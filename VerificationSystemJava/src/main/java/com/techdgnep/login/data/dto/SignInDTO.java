package com.techdgnep.login.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInDTO {

    @Email
    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
