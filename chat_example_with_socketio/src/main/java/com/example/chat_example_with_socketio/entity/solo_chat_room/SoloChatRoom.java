package com.example.chat_example_with_socketio.entity.solo_chat_room;

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
@NoArgsConstructor
@AllArgsConstructor
public class SoloChatRoom {
    @Id
    private String chatId;

    private Integer userId;

    private String friendsInfo;

    private String deviceToken;

    private String friendsImageName;
}
