package com.maka.service;

public interface SmsService {
    void sendSms(String phoneNumber, String familyName, String location,
                  String elderlyName, String gender, String age, String contactNumber);
}
