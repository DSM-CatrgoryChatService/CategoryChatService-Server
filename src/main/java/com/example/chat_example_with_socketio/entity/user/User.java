package com.example.chat_example_with_socketio.entity.user;

import com.example.chat_example_with_socketio.entity.user.enums.Sex;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String info;

    private String name;

    private String id;

    private String password;

    private Sex sex;

    public User setInfo(String info) {
        this.info = info;
        return this;
    }
}
