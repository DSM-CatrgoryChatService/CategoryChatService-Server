package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String id;
    private String password;
}
