package com.example.chat_example_with_socketio.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RequiredArgsConstructor
public class ImageResponse {
    @SneakyThrows
    public byte[] getUserImage(String imageName, String imageDir) {

        File file = new File(imageDir, imageName);
        if(!file.exists()) throw new RuntimeException();

        InputStream inputStream = new FileInputStream(file);

        return IOUtils.toByteArray(inputStream);
    }
}
