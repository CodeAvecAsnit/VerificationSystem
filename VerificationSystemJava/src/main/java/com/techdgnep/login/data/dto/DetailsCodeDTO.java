package com.techdgnep.login.data.dto;

import com.techdgnep.login.exception.custom.OutofTrieError;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailsCodeDTO {

    @Email
    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @Min(100000)
    @Max(999999)
    private int code;

    public DetailsCodeDTO(){
        this.tries = 0;
    }

    public DetailsCodeDTO(String userName,
            /*Encrypt this for better security */String password,
                          /* Hash this and compare for security reasons.*/int code){
        this.userName = userName;
        this.password = password;
    }

    /**
     * for rate limiting
     */
    private int tries;

    /**
     * Compare the given code
     * Throw Error if the tries exceeds the count.
     * @return true if the code matches and false if it doesn't also increment the tries if it does not match
     */
    public boolean compareCode(int userCode){
        if(tries>5) throw new OutofTrieError("Max rate reached");
        ++tries;
        return code==userCode;
    }
}
