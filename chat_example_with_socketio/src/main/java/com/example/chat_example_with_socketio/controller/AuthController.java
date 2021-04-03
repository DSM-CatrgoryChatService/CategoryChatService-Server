package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.payload.request.SignInRequest;
import com.example.chat_example_with_socketio.payload.response.TokenResponse;
import com.example.chat_example_with_socketio.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public TokenResponse signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    @PutMapping
    public TokenResponse refreshToken(@RequestHeader("Authorization") String token) {
        return authService.refreshToken(token);
    }
}
