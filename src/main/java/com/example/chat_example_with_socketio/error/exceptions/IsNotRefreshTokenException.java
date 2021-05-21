package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class IsNotRefreshTokenException extends CustomException {
    public IsNotRefreshTokenException() {
        super(ErrorCode.IS_NOT_REFRESH_TOKEN);
    }
}
