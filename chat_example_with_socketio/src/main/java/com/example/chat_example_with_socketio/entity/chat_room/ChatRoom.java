package com.example.chat_example_with_socketio.entity.chat_room;

import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
import com.example.chat_example_with_socketio.entity.chat_room.enums.ChatCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String chatRoomId;

    private String title;

    private Integer userId;

    private Authority authority;

    private ChatCategory chatCategory;

    public ChatRoom update(String title) {
        this.title = title;

        return this;
    }
}
