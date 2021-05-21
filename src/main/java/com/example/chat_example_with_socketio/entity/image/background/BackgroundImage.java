package com.example.chat_example_with_socketio.entity.image.background;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BackgroundImage {
    @Id
    private Integer userId;

    private String imageName;

    public BackgroundImage updateBackground(String imageName) {
        this.imageName = imageName;

        return this;
    }
}
