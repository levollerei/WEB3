package com.maka.pojo;

public class SmsRequest {
    private String phoneNumber; // 手机号
    private String verificationCode; // 验证码或短信内容

    // Getters 和 Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
