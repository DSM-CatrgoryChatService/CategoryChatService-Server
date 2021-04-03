package com.example.chat_example_with_socketio.security;

import com.example.chat_example_with_socketio.error.exceptions.IsNotRefreshTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accesstoken}")
    private Integer accessExpiration;

    @Value("${jwt.refreshtoken}")
    private Integer refreshToken;

    public String generateAccessToken(Integer userId) {
        return Jwts.builder()
                .claim("type", "access_token")
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration * 1000))
                .compact();
    }

    public String generateRefreshToken(Integer userId) {
        return Jwts.builder()
                .claim("type", "access_token")
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshToken * 1000))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token)
                    .getBody().getSubject();

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        if(validateToken(token)) {
            throw new IsNotRefreshTokenException();
        }

        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJwt(token).getBody().get("type").equals("refresh_token");
    }

    public Integer getUserId(String token) {
        return Integer.parseInt(Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject());
    }
}
