package com.saksham.loglens.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secretKey;

    public String tokenGenerator(String email) {
        String token = Jwts.builder().setSubject(email).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 64800000)).signWith(getSignKey()).compact();

        return token;
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(getSecretKey().getBytes());
    }

    private String getSecretKey() {
        return secretKey;
    }

    public String userNameExtractor(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    private Date tokenExpirationExtractor(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if(!userNameExtractor(token).equals(userDetails.getUsername())) return false;

        return new Date().before(tokenExpirationExtractor(token));
    }
}
