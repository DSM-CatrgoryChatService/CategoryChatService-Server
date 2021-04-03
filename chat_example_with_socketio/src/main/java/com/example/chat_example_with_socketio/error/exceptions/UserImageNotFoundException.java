package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class UserImageNotFoundException extends CustomException {
    public UserImageNotFoundException() {
        super(ErrorCode.USER_IMAGE_NOT_FOUND);
    }
}
