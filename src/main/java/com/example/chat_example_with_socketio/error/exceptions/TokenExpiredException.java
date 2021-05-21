package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class TokenExpiredException extends CustomException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}
