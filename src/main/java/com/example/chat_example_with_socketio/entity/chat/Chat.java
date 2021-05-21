package com.example.chat_example_with_socketio.entity.chat;

import com.example.chat_example_with_socketio.entity.chat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    private Integer id;

    private String chatId;

    private Integer userId;

    private String userName;

    private String message;

    private MessageType messageType;

    private LocalDateTime writeAt;

    public Chat updateMessage(String message) {
        this.message = message;

        return this;
    }
}
