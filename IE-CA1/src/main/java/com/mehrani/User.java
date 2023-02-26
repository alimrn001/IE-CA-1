package com.mehrani;

import java.time.LocalDate;
import java.util.Date;

public class User {
    private String username;
    private String password;
    private LocalDate birthday;
    private String email;
    private String address;
    private double credit;


    void setUserData(String username_, String password_, String birthday_, String email_, String address_, double credit_, boolean isUpdate) {
        if(isUpdate)
            this.username = username_;
        this.password = password_;
        this.birthday = LocalDate.parse(birthday_);
        this.email = email_;
        this.address = address_;
        this.credit = credit_;
    }
    void setUsername(String username_) {
        this.username = username_;
    }
    void setPassword(String password_) {
        this.password = password_;
    }
    public void setBirthday(String birthday_) {
        this.birthday = LocalDate.parse(birthday_);
    }
    public void setEmail(String email_) {
        this.email = email_;
    }
    public void setAddress(String address_) {
        this.address = address_;
    }
    public void setCredit(double credit_) {
        this.credit = credit_;
    }
    public LocalDate getBirthday() {
        return this.birthday;
    }
    public double getCredit() {
        return this.credit;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public String getEmail() {
        return this.email;
    }
    public String getAddress() {
        return this.address;
    }

}
