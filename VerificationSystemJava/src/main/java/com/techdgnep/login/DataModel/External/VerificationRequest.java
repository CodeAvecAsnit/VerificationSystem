package com.techdgnep.login.DataModel.External;

public class VerificationRequest {
    private String email;
    private int verificationCode;

    public VerificationRequest(){
    }

    public VerificationRequest(String email, int verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }
}
