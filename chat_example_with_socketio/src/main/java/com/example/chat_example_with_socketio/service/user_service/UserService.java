package com.example.chat_example_with_socketio.service.user_service;

import com.example.chat_example_with_socketio.payload.request.SignUpRequest;
import com.example.chat_example_with_socketio.payload.response.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
    byte[] getUserImage(String imageName);
    void updateUserImage(MultipartFile image, String token);
    void updateBackgroundImage(MultipartFile background, String token);
    void setBackground(MultipartFile background, String id);
    UserInfoResponse getMyInfo(String token);
}
