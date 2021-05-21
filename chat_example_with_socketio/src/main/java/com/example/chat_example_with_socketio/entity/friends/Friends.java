package com.example.chat_example_with_socketio.entity.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Friends {
    @Id
    private Integer friendId;

    private Integer userId;

    private boolean isFriendly;

    public Friends block(boolean isFriendly) {
        this.isFriendly = isFriendly;
        return this;
    }
}
