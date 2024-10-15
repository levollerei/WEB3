package com.maka.service.impl;

import com.maka.service.VoiceService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class VoiceServiceImpl implements VoiceService {

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
        // 将音频数据转换为 Base64 编码
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);

        // 创建请求体
        String jsonBody = String.format("{\"voiceId\":\"%s\", \"audio\":\"%s\"}", voiceId, audioBase64);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        // 创建请求
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-Appid", appId)
                .addHeader("X-CurTime", String.valueOf(System.currentTimeMillis() / 1000))
                .addHeader("X-Param", "base64") // 根据需求添加参数
                .addHeader("X-CheckSum", calculateCheckSum(apiSecret, audioBase64)) // 计算校验和
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return false; // 处理错误
            }

            String responseBody = response.body().string();
            return parseResponse(responseBody); // 解析响应
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 处理异常
        }
    }

    private String calculateCheckSum(String apiSecret, String audioBase64) {
        // 实现校验和的具体计算逻辑
        // 按照讯飞 API 文档中的说明来生成检验和
        // 示例代码：return someChecksumValue;
        return "your-checksum"; // 替换为实际的校验和
    }

    private boolean parseResponse(String responseBody) {
        // 根据 API 返回的 JSON 格式解析并判断认证结果
        // 示例代码，需替换为具体逻辑
        return responseBody.contains("认证成功"); // 示例逻辑
    }
}
