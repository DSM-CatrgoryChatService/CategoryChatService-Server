package com.example.chat_example_with_socketio.util;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class AES256 {

    private static volatile AES256 instance;

    @Value("${SECRET_KEY}")
    private String secretKey;
    static String iv;

    public static AES256 getInstance() {
        if(instance == null) {
            synchronized (AES256.class) {
                if(instance == null) {
                    instance = new AES256();
                }
            }
        }
        return instance;
    }

    private AES256() {
        iv = secretKey.substring(0,16);
    }

    public String AES_Encode(String str) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException {

        byte[] keyData = secretKey.getBytes();

        SecretKey secretKey = new SecretKeySpec(keyData, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

        byte[] encrypted = str.getBytes();

        return new String(Base64.encodeBase64(encrypted));
    }

    public String AES_Decode(String str) {
        try {
            byte[] keyData = secretKey.getBytes();

            SecretKey secretKey = new SecretKeySpec(keyData, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

            byte[] decodingStr = Base64.decodeBase64(str.getBytes());

            return new String(decodingStr, StandardCharsets.UTF_8);
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
