package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class LoginFailException extends CustomException {
    public LoginFailException() {
        super(ErrorCode.LOGIN_FAIL);
    }
}
