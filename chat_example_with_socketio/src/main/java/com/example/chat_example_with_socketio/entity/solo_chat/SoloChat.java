package com.example.chat_example_with_socketio.entity.solo_chat;

import com.example.chat_example_with_socketio.entity.chat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SoloChat {
    @Id
    private Integer id;

    private String userName;

    private String message;

    private String chatId;

    private Integer userId;

    private MessageType messageType;

    private LocalDateTime writeAt;

    private String userImageName;

    public SoloChat updateChat(String message) {
        this.message = message;

        return this;
    }
}
