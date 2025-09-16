package com.example.movie_app.services;

import com.example.movie_app.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String SignKey;

    @Value("${app.jwt.expiration-ms}")
    private Long expiration;

    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(this.SignKey);
//        return Keys.hmacShaKeyFor(keyBytes);
        return Keys.hmacShaKeyFor(SignKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.expiration);
        Map<String, String> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        try{
            String token = Jwts.builder()
                    .setClaims(extraClaims)
                    .setIssuedAt(now)
                    .setSubject(user.getUsername())
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS256, getSignInKey())
                    .setIssuer("virgo0239")
                    .setAudience("web-client")
                    .compact();
            return token;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired", e);
        }catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token");
        }
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
