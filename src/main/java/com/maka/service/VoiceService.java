package com.maka.service;

public interface VoiceService {
    boolean authenticateUser(String voiceId, byte[] audioData);

    String authenticateWithAll(byte[] audioData);

    void saveVoicePrint(String userId, String voiceId, String audioBase64);
}
