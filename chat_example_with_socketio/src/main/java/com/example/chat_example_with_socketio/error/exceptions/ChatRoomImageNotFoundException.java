package com.example.chat_example_with_socketio.error.exceptions;


import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;

public class ChatRoomImageNotFoundException extends CustomException {
    public ChatRoomImageNotFoundException() {
        super(ErrorCode.CHATROOM_IMAGE_NOT_FOUND);
    }
}
