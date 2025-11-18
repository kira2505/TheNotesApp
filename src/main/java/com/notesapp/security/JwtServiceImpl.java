package com.notesapp.security;

import com.notesapp.model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;

    public JwtServiceImpl(@Value("${jwttoken.sign.secret.key}") String jjwtSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jjwtSecretKey));
    }

    public String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        return generateToken(claims, user);
    }

    private String generateToken(Map<String, Object> claims, AppUser user) {
        return Jwts.builder()
                .claims()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (3600 * 1000)))
                .subject(user.getUsername())
                .add(claims)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
