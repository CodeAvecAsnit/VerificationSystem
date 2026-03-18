package com.autowhouse.loginservice.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
