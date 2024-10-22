package com.ilkinmehdiyev.myticket.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  //    1. Generate token

  @Value("${security.jwt.accessToken.secret}")
  private String accessTokenSecret;

  @Value("${security.jwt.refreshToken.secret}")
  private String refreshTokenSecret;

  @Value("${security.jwt.accessToken.expiration}")
  private long accessTokenExpiration;

  @Value("${security.jwt.refreshToken.expiration}")
  private long refreshTokenExpiration;

  public String generateAccessToken(String email) {
    return generateToken(email, accessTokenSecret, accessTokenExpiration);
  }

  public String generateRefreshToken(String email) {
    return generateToken(email, refreshTokenSecret, refreshTokenExpiration);
  }

  public String generateToken(String email, String secret, long expirationMS /*custom Claims*/) {
    long currentTimeMillis = System.currentTimeMillis();
    return Jwts.builder()
        //            .claims()
        .subject(email)
        .issuedAt(new Date(currentTimeMillis))
        .expiration(new Date(currentTimeMillis + expirationMS))
        .signWith(getSignedKey(secret))
        .compact();
  }

  private SecretKey getSignedKey(String secret) {
    byte[] bytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(bytes);
  }

  public String extractUsername(String token, boolean isAccessToken) {
    Claims claims = extractClaims(token, isAccessToken);
    return claims.getSubject();
  }

  public Claims extractClaims(String token, boolean isAccessToken) {
    return Jwts.parser()
        .verifyWith(getSignedKey(isAccessToken ? accessTokenSecret : refreshTokenSecret))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean isTokenValid(String jwtToken, boolean isAccessToken) {
    Date date = extractExpirationDate(jwtToken, isAccessToken);
    return Objects.nonNull(date) && date.before(new Date());
  }

  private Date extractExpirationDate(String jwtToken, boolean isAccessToken) {
    Claims claims = extractClaims(jwtToken, isAccessToken);
    return claims.getExpiration();
  }
}
