package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class MessageNotFoundException extends CustomException {
    public MessageNotFoundException() {
        super(ErrorCode.CHAT_NOT_FOUND);
    }
}
