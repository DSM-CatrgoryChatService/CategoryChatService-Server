package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.entity.user.enums.Sex;
import com.example.chat_example_with_socketio.payload.request.SignUpRequest;
import com.example.chat_example_with_socketio.payload.response.UserInfoResponse;
import com.example.chat_example_with_socketio.service.user_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public UserInfoResponse getMyInfo(@RequestHeader("Authorization") String token) {
        return userService.getMyInfo(token);
    }

    @GetMapping(value = "/image/{imageName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getUserImage(@PathVariable String imageName){
        return userService.getUserImage(imageName);
    }

    @PostMapping("/background")
    public void setBackground(@RequestParam MultipartFile background,
                              @RequestParam String id) {
        userService.setBackground(background, id);
    }

    @PostMapping
    public void signIn(@RequestParam String name,
                       @RequestParam String id,
                       @RequestParam String deviceToken,
                       @RequestParam String password,
                       @RequestParam Sex sex,
                       @RequestParam MultipartFile image) {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .id(id)
                .image(image)
                .password(password)
                .sex(sex)
                .name(name)
                .userDeviceToken(deviceToken)
                .build();

        userService.signUp(signUpRequest);
    }

    @PutMapping
    public void updateImage(@RequestParam MultipartFile image,
                            @RequestHeader("Authorization") String token) {
        userService.updateUserImage(image, token);
    }

    @PutMapping("/background")
    public void updateBackground(@RequestParam MultipartFile image,
                                 @RequestHeader("Authorization") String token) {
        userService.updateBackgroundImage(image, token);
    }
}
