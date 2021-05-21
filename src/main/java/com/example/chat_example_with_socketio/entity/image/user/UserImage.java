package com.example.chat_example_with_socketio.entity.image.user;

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
public class UserImage {
    @Id
    Integer userId;

    String imageName;

    public UserImage updateImage(String imageName) {
        this.imageName = imageName;

        return this;
    }
}
