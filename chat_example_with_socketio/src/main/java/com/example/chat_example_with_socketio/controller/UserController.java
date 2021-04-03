package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.entity.user.enums.Sex;
import com.example.chat_example_with_socketio.payload.request.SignUpRequest;
import com.example.chat_example_with_socketio.payload.response.UserInfoResponse;
import com.example.chat_example_with_socketio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserInfoResponse getInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserInfo(token);
    }

    @GetMapping("/image/{imageName}")
    public byte[] getUserImage(@PathVariable String imageName) {
        return userService.getUserImage(imageName);
    }

    @PostMapping
    public void signIn(@RequestParam String name,
                       @RequestParam String id,
                       @RequestParam String password,
                       @RequestParam Sex sex,
                       @RequestParam MultipartFile image) {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .id(id)
                .image(image)
                .password(password)
                .sex(sex)
                .name(name)
                .build();

        userService.signUp(signUpRequest);
    }

    @PutMapping
    public void updateImage(@RequestParam MultipartFile image,
                            @RequestHeader("Authorization") String token) {
        userService.updateUserImage(image, token);
    }
}
