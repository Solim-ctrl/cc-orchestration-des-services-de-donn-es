package eshop.ms.basket.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private static final String secretKey = "secureKey1234567890";

    // Generate a JWT token (utility method for testing or other purposes)
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1-hour expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
