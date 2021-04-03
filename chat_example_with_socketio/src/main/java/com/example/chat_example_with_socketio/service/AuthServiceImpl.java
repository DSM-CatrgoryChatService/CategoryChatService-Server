package com.example.chat_example_with_socketio.service;

import com.example.chat_example_with_socketio.entity.token.Token;
import com.example.chat_example_with_socketio.entity.token.repository.TokenRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.LoginFailException;
import com.example.chat_example_with_socketio.error.exceptions.RefreshTokenNotFoundException;
import com.example.chat_example_with_socketio.payload.request.SignInRequest;
import com.example.chat_example_with_socketio.payload.response.TokenResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import com.example.chat_example_with_socketio.util.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final JwtProvider jwtProvider;
    private final AES256 aes256;

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        return userRepository.findById(signInRequest.getId())
                .filter(user -> aes256.AES_Decode(user.getPassword()).equals(signInRequest.getPassword()))
                .map(User::getUserId)
                .map(user -> {
                    String accessToken = jwtProvider.generateAccessToken(user);
                    String refreshToken = jwtProvider.generateRefreshToken(user);

                    tokenRepository.save(
                            Token.builder()
                            .refreshToken(refreshToken)
                            .userId(user)
                            .build()
                    );

                    return TokenResponse.builder()
                            .accessToken(accessToken)
                            .refresjToken(refreshToken)
                            .build();
                })
                .orElseThrow(LoginFailException::new);
    }

    @Override
    public TokenResponse refreshToken(String token) {
        if(jwtProvider.isRefreshToken(token)) {
            throw new RuntimeException();
        }

        return tokenRepository.findByRefreshToken(token)
                .map(token1 -> {
                    String newRefreshToken = jwtProvider.generateRefreshToken(token1.getUserId());

                    return token1.update(newRefreshToken);
                })
                .map(tokenRepository::save)
                .map(token1 -> {
                    String accessToken = jwtProvider.generateAccessToken(token1.getUserId());

                    return TokenResponse.builder()
                            .accessToken(accessToken)
                            .refresjToken(token1.getRefreshToken())
                            .build();
                })
                .orElseThrow(RefreshTokenNotFoundException::new);
    }
}
