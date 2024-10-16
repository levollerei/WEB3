package com.maka.controller;

import com.maka.pojo.VoiceRequest;
import com.maka.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

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

    @PostMapping("/authenticateAll")
    public ResponseEntity<?> authenticateWithAll(@RequestParam("audioFile") MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            return ResponseEntity.badRequest().body("音频文件不能为空");
        }

        try {
            byte[] audioData = audioFile.getBytes();
            String result = voiceService.authenticateWithAll(audioData);
            return ResponseEntity.ok(new ResponseMessage(result));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseMessage("文件处理失败: " + e.getMessage()));
        }
    }

    @PostMapping("/registerVoice")
    public ResponseEntity<?> registerVoice(@RequestParam("userId") String userId,
                                           @RequestParam("voiceId") String voiceId,
                                           @RequestParam("audioFile") MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            return ResponseEntity.badRequest().body("音频文件不能为空");
        }

        try {
            // 读取音频文件数据
            byte[] audioData = audioFile.getBytes();
            // 将音频数据转换为 Base64
            String audioBase64 = Base64.getEncoder().encodeToString(audioData);

            // 保存声纹到临时数组
            voiceService.saveVoicePrint(userId, voiceId, audioBase64);

            // 返回成功的响应
            return ResponseEntity.ok(new ResponseMessage("声纹注册成功"));
        } catch (IOException e) {
            e.printStackTrace();
            // 如果文件处理出现问题，返回错误响应
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
