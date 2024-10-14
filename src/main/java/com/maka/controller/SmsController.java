package com.maka.controller;

import com.maka.pojo.SmsRequest; // 引入 SmsRequest 类
import com.maka.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public String sendSms(@RequestBody SmsRequest request) {
        // 获取手机号和验证码
        String phoneNumber = request.getPhoneNumber();
        String verificationCode = request.getVerificationCode(); // 这个可以来自前端请求

        // 调用短信服务发送短信
        smsService.sendSms(phoneNumber, verificationCode);
        return "短信已发送";
    }
}
