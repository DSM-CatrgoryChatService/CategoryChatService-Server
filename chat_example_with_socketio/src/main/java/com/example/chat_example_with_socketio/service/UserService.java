package com.example.chat_example_with_socketio.service;

import com.example.chat_example_with_socketio.payload.request.SignUpRequest;
import com.example.chat_example_with_socketio.payload.response.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
    UserInfoResponse getUserInfo(String token);
    byte[] getUserImage(String imageName);
    void updateUserImage(MultipartFile image, String token);
}
