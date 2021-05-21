package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class UserAlreadySignedException extends CustomException {
    public UserAlreadySignedException() {
        super(ErrorCode.USER_ALREADY_SIGNED);
    }
}
