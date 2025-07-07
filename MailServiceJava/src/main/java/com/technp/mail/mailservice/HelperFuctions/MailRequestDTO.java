package com.technp.mail.mailservice.HelperFuctions;

public class MailRequestDTO {
    private String email;

    public MailRequestDTO() {
    }

    public MailRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
