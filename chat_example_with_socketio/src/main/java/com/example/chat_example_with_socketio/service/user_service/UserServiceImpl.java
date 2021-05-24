package com.example.chat_example_with_socketio.service.user_service;

import com.example.chat_example_with_socketio.entity.image.background.BackgroundImage;
import com.example.chat_example_with_socketio.entity.image.background.repository.BackgroundImageRepository;
import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.UserDeviceToken;
import com.example.chat_example_with_socketio.entity.user.repository.UserDeviceTokenRepository;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.*;
import com.example.chat_example_with_socketio.payload.request.SignUpRequest;
import com.example.chat_example_with_socketio.payload.response.UserInfoResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import com.example.chat_example_with_socketio.util.AES256;
import com.example.chat_example_with_socketio.util.ImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;
    private final BackgroundImageRepository backgroundImageRepository;

    private final JwtProvider jwtProvider;
    private final AES256 aes256;

    private final ImageResponse imageResponse;

    @Value("${image.dir}")
    private String imageDir;

    @SneakyThrows
    @Override
    public byte[] getUserImage(String imageName) {
        userImageRepository.findByImageName(imageName)
                .orElseThrow(UserImageNotFoundException::new);

        return imageResponse.getUserImage(imageName, imageDir);
    }

    @SneakyThrows
    @Override
    public byte[] getBackground(String imageName) {
        backgroundImageRepository.findByImageName(imageName)
                .orElseThrow(UserImageNotFoundException::new);

        return imageResponse.getUserImage(imageName, imageDir);
    }

    @SneakyThrows
    @Override
    public void setBackground(MultipartFile background, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if(background.isEmpty()) {
            backgroundImageRepository.save(
                    BackgroundImage.builder()
                            .imageName("null")
                            .userId(user.getUserId())
                            .build()
            );
        }else {
            if(background.getOriginalFilename() == null || background.getOriginalFilename().length() == 0)
                throw new UserImageNotRequestException();

            int idx = background.getOriginalFilename().indexOf(".");
            String ex = background.getOriginalFilename().substring(idx + 1);

            if(!(ex.contains("jpg") || ex.contains("png") || ex.contains("jpeg")))
                throw new BadExRequestException();

            BackgroundImage backgroundImage = backgroundImageRepository.save(
                    BackgroundImage.builder()
                            .userId(user.getUserId())
                            .imageName(UUID.randomUUID().toString() + "." + ex)
                            .build()
            );

            background.transferTo(new File(imageDir, backgroundImage.getImageName()));
        }
    }

    @Override
    public UserInfoResponse getMyInfo(String token) {
        User my = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        UserImage profile = userImageRepository.findByUserId(my.getUserId())
                .orElseThrow(UserImageNotFoundException::new);

        BackgroundImage backgroundImage = backgroundImageRepository.findByUserId(my.getUserId())
                .orElseThrow(UserImageNotFoundException::new);

        return UserInfoResponse.builder()
                .name(my.getName())
                .myCode(my.getInfo())
                .sex(my.getSex())
                .profileImageName(profile.getImageName())
                .backgroundImageName(backgroundImage.getImageName())
                .build();
    }

    @SneakyThrows
    @Override
    public void signUp(SignUpRequest signUpRequest) {
        User checkUser = userRepository.findById(signUpRequest.getId())
                .orElse(null);

        if(checkUser != null) {
            throw new UserAlreadySignedException();
        }

        User user = userRepository.save(
                User.builder()
                .name(signUpRequest.getName())
                .sex(signUpRequest.getSex())
                .id(signUpRequest.getId())
                .password(aes256.AES_Encode(signUpRequest.getPassword()))
                .build()
        );

        String info = aes256.AES_Encode(user.getUserId().toString());

        userRepository.save(
                user.setInfo(info)
        );

        userDeviceTokenRepository.save(
                UserDeviceToken.builder()
                        .name(signUpRequest.getName())
                        .userId(user.getUserId())
                        .token(signUpRequest.getUserDeviceToken())
                        .build()
        );



        if(signUpRequest.getImage().isEmpty()) {
            userImageRepository.save(
                    UserImage.builder()
                    .userId(user.getUserId())
                    .imageName("null")
                    .build()
            );
        }else {
            if(signUpRequest.getImage().getOriginalFilename() == null || signUpRequest.getImage().getOriginalFilename().length() == 0)
                throw new UserImageNotRequestException();

            int idx = signUpRequest.getImage().getOriginalFilename().indexOf(".");
            String ex = signUpRequest.getImage().getOriginalFilename().substring(idx + 1);

            System.out.println(signUpRequest.getImage().getOriginalFilename());
            System.out.println(ex);
            System.out.println(idx);

            if(!(ex.contains("jpg")||ex.contains("png")||ex.contains("jpeg")))
                throw new BadExRequestException();

            UserImage userImage = userImageRepository.save(
                    UserImage.builder()
                    .imageName(UUID.randomUUID().toString() + "." + ex)
                    .userId(user.getUserId())
                    .build()
            );

            signUpRequest.getImage().transferTo(new File(imageDir, userImage.getImageName()));
        }

    }

    @SneakyThrows
    @Override
    public void updateUserImage(MultipartFile image, String token) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        UserImage myImage = userImageRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserImageNotFoundException::new);

        if(image.isEmpty())
            throw new UserImageNotRequestException();

        File file = new File(imageDir, myImage.getImageName());
        if(file.exists()) file.delete();

        userImageRepository.save(
                myImage.updateImage(UUID.randomUUID().toString())
        );

        image.transferTo(new File(imageDir, myImage.getImageName()));
    }

    @SneakyThrows
    @Override
    public void updateBackgroundImage(MultipartFile background, String token) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        BackgroundImage image = backgroundImageRepository.findByUserId(user.getUserId())
                .orElseThrow(UserImageNotFoundException::new);

        if(background.isEmpty())
            throw new UserImageNotRequestException();

        File file = new File(imageDir, image.getImageName());
        if(file.exists()) file.delete();

        backgroundImageRepository.save(
                image.updateBackground(UUID.randomUUID().toString())
        );

        background.transferTo(new File(imageDir, image.getImageName()));
    }
}
