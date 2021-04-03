package com.example.chat_example_with_socketio.entity.image.chatRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomImage {
    @Id
    private String chatId;

    private String imageName;

    public ChatRoomImage update(String imageName) {
        this.imageName = imageName;

        return this;
    }
}
