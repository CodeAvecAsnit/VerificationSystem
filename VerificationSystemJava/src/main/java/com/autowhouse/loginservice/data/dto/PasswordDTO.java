package com.autowhouse.loginservice.data.dto;

import com.autowhouse.loginservice.data.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordDTO {
    @Email(message = "Not a valid email")
    private String email;
    private String oldPassword;
    @ValidPassword(message = "Password should contain at least one uppercase, one symbol")
    private String newPassword;

    public boolean areSamePasswords(){
        return this.oldPassword.equals(this.newPassword) ||
                this.oldPassword == this.newPassword;
    }
}
