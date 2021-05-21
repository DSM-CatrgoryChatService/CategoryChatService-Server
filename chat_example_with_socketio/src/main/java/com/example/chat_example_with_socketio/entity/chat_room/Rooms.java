package com.example.chat_example_with_socketio.entity.chat_room;

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
public class Rooms {
    @Id
    private String chatId;

    private LocalDateTime createAt;

    private Integer chatLength;

    public Rooms addLength() {
        this.chatLength++;

        return this;
    }
}
