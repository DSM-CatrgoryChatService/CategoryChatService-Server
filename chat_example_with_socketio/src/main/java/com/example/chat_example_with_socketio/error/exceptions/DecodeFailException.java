package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class DecodeFailException extends CustomException {
    public DecodeFailException() {
        super(ErrorCode.DECODE_FAIL);
    }
}
