package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class UserImageNotRequestException extends CustomException {
    public UserImageNotRequestException() {
        super(ErrorCode.USER_IMAGE_NOT_REQUEST);
    }
}
