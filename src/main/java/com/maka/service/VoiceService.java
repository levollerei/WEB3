package com.maka.service;

public interface VoiceService {
    boolean authenticateUser(String voiceId, byte[] audioData);
}
