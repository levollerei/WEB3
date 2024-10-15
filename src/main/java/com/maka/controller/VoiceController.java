package com.maka.controller;

import com.maka.pojo.VoiceRequest;
import com.maka.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {

    @Autowired
    private VoiceService voiceService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@ModelAttribute VoiceRequest voiceRequest) {
        // 参数验证
        if (voiceRequest.getVoiceId() == null || voiceRequest.getVoiceId().isEmpty()) {
            return ResponseEntity.badRequest().body("声纹 ID 不能为空");
        }
        if (voiceRequest.getAudioFile() == null || voiceRequest.getAudioFile().isEmpty()) {
            return ResponseEntity.badRequest().body("音频文件不能为空");
        }

        try {
            byte[] audioData = voiceRequest.getAudioFile().getBytes();
            boolean isAuthenticated = voiceService.authenticateUser(voiceRequest.getVoiceId(), audioData);

            return ResponseEntity.ok().body(isAuthenticated ?
                new ResponseMessage("认证成功") :
                new ResponseMessage("认证失败"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseMessage("文件处理失败: " + e.getMessage()));
        }
    }

    // 创建一个内部类来封装返回消息
    static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
