package com.example.chat_example_with_socketio.service;

import com.example.chat_example_with_socketio.payload.request.SignInRequest;
import com.example.chat_example_with_socketio.payload.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest signInRequest);
    TokenResponse refreshToken(String token);
}
