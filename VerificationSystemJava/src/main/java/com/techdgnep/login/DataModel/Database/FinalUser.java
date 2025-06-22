package com.techdgnep.login.DataModel.Database;


import jakarta.persistence.*;

@Entity
public class FinalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RegisterId;
    @Column(nullable = false)
    private String userName;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String passcode;

    public FinalUser(String email, String userName, String passcode,Long registerId) {
        this.RegisterId =registerId;
        this.email = email;
        this.userName = userName;
        this.passcode = passcode;
    }

    public FinalUser() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRegisterId() {
        return RegisterId;
    }

    public void setRegisterId(Long registerId) {
        RegisterId = registerId;
    }
}
