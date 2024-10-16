package com.maka.service.impl;

import com.maka.pojo.VoicePrint;
import com.maka.service.VoiceService;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class VoiceServiceImpl implements VoiceService {

    // 存储声纹的临时数组
    private List<VoicePrint> voicePrints = new ArrayList<>();

    @Value("${xfyun.appid}")
    private String appId;

    @Value("${xfyun.apiKey}")
    private String apiKey;

    @Value("${xfyun.apiSecret}")
    private String apiSecret;

    @Value("${xfyun.apiUrl}")
    private String apiUrl;

    @Override
    public boolean authenticateUser(String voiceId, byte[] audioData) {
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);
        System.out.println("Authenticating for Voice ID: " + voiceId);
        System.out.println("Audio Feature (Base64): " + audioBase64); // 输出
        String jsonBody = String.format("{\"voiceId\":\"%s\", \"audio\":\"%s\"}", voiceId, audioBase64);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-Appid", appId)
                .addHeader("X-CurTime", String.valueOf(System.currentTimeMillis() / 1000))
                .addHeader("X-Param", "base64")
                .addHeader("X-CheckSum", calculateCheckSum(apiSecret, audioBase64))
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return false;
            }
            String responseBody = response.body().string();
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + responseBody); // 打印详细的响应
            return parseResponse(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String authenticateWithAll(byte[] audioData) {
        List<String> allVoiceIds = getAllVoiceIdsFromDatabase(); // 假设有一个方法获取所有声纹 ID

        for (String voiceId : allVoiceIds) {
            if (authenticateUser(voiceId, audioData)) {
                return "认证成功，声纹 ID: " + voiceId;
            }
        }
        return "认证失败";
    }

    public void saveVoicePrint(String userId, String voiceId, String audioBase64) {
        System.out.println("Saving VoicePrint for userId: " + userId + ", voiceId: " + voiceId);
        System.out.println("Voice Feature (Base64): " + audioBase64); // 输出
        VoicePrint voicePrint = new VoicePrint();
        voicePrint.setUserId(userId);
        voicePrint.setVoiceId(voiceId);
        voicePrint.setVoiceFeature(audioBase64);
        voicePrints.add(voicePrint); // 添加到临时数组
    }



    private String calculateCheckSum(String apiSecret, String audioBase64) {
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        String checkSum = apiSecret + curTime + audioBase64; // 示例，可以按实际API要求进行调整
        return DigestUtils.md5Hex(checkSum); // 使用Apache Commons Codec计算MD5
    }

    private boolean parseResponse(String responseBody) {
        // 增强解析逻辑
        if (responseBody.contains("认证成功")) {
            return true;
        } else if (responseBody.contains("失败")) {
            System.out.println("认证失败，响应内容: " + responseBody);
            return false;
        }
        return false;
    }

    private List<String> getAllVoiceIdsFromDatabase() {
        List<String> voiceIds = new ArrayList<>();
        for (VoicePrint voicePrint : voicePrints) {
            voiceIds.add(voicePrint.getVoiceId());
        }
        return voiceIds;  // 返回所有声纹 ID 的列表
    }

}
