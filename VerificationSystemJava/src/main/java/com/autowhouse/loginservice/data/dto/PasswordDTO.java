package com.autowhouse.loginservice.data.dto;

import com.autowhouse.loginservice.data.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordDTO {
    private String userName;
    private String oldPassword;
    @ValidPassword
    private String newPassword;
}
