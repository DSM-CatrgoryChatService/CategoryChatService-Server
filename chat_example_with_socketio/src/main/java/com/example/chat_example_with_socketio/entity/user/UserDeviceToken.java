package com.example.chat_example_with_socketio.entity.user;

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
public class UserDeviceToken {
    @Id
    private Integer userId;

    private String name;

    private String token;
}
