package com.example.chat_example_with_socketio.error;

import com.example.chat_example_with_socketio.error.exception.CustomException;
import com.example.chat_example_with_socketio.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(final CustomException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()),
                HttpStatus.valueOf(errorCode.getStatus()));
    }
}
