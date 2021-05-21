package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
