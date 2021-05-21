package com.example.chat_example_with_socketio.payload.request;

import com.example.chat_example_with_socketio.entity.user.enums.Sex;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class SignUpRequest {
    private String name;
    private String id;
    private String password;
    private Sex sex;
    private MultipartFile image;
    private String userDeviceToken;
}
