package com.example.chat_example_with_socketio.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DECODE_FAIL(400, "password decode fail"),
    USER_IMAGE_NOT_REQUEST(400, "bad request"),
    USER_NOT_FOUND(403, "user not found"),
    USER_ALREADY_SIGNED(403, "user already signed"),
    LOGIN_FAIL(403, "login fail"),
    INVALID_TOKEN(403, "invalid token"),
    USER_IMAGE_NOT_FOUND(404, "user image not found"),
    REFRESH_TOKEN_NOT_FOUND(404, "refresh_token not found"),
    CHAT_NOT_FOUND(404, "message not found"),
    CHATROOM_NOT_FOUND(404, "chat room not found"),
    CHATROOM_IMAGE_NOT_FOUND(404, "chatroom not found"),
    IS_NOT_REFRESH_TOKEN(404, "is not refresh token"),
    BAD_EX_REQUEST(400, "bad request ex"),
    TOKEN_EXPIRED(403, "token expired");

    private int status;
    private String message;
}
