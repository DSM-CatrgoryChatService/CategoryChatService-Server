package com.example.chat_example_with_socketio.error.exceptions;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class ChatRoomNotFoundException extends CustomException {
    public ChatRoomNotFoundException() {
        super(ErrorCode.CHATROOM_NOT_FOUND);
    }
}
