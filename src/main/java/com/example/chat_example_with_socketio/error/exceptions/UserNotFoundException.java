package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
