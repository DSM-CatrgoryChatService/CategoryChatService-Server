package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class BadExRequestException extends CustomException {
    public BadExRequestException() {
        super(ErrorCode.BAD_EX_REQUEST);
    }
}
