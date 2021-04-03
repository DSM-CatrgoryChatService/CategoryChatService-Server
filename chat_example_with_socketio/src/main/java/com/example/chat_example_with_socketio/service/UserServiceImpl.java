package com.example.chat_example_with_socketio.service;

import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.UserAlreadySignedException;
import com.example.chat_example_with_socketio.error.exceptions.UserImageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.UserImageNotRequestException;
import com.example.chat_example_with_socketio.error.exceptions.UserNotFoundException;
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
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    private final JwtProvider jwtProvider;
    private final AES256 aes256;

    private final ImageResponse imageResponse;

    @Value("${image.dir}")
    private String imageDir;

    @Override
    public UserInfoResponse getUserInfo(String token) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        UserImage userImage = userImageRepository.findByUserId(user.getUserId())
                .orElseThrow(UserImageNotFoundException::new);

        return UserInfoResponse.builder()
                .name(user.getName())
                .sex(user.getSex())
                .imageName(userImage.getImageName())
                .build();
    }

    @SneakyThrows
    @Override
    public byte[] getUserImage(String imageName) {
        userImageRepository.findByImageName(imageName)
                .orElseThrow(UserImageNotFoundException::new);

        return imageResponse.getUserImage(imageName, imageDir);
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

        if(signUpRequest.getImage().isEmpty()) {
            userImageRepository.save(
                    UserImage.builder()
                    .userId(user.getUserId())
                    .imageName("null")
                    .build()
            );
        }else {
            UserImage userImage = userImageRepository.save(
                    UserImage.builder()
                    .imageName(UUID.randomUUID().toString())
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

        userImageRepository.findByImageName(myImage.getImageName());

        UserImage newImage = userImageRepository.save(
                UserImage.builder()
                .userId(jwtProvider.getUserId(token))
                .imageName(UUID.randomUUID().toString())
                .build()
        );

        image.transferTo(new File(imageDir, newImage.getImageName()));
    }
}
