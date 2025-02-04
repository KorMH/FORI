package com.foodri.foodreview.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

//@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  private Key key;
  private final long accessTokenValidity;
  private final long refreshTokenValidity;

  public JwtTokenProvider(
      @Value("${jwt.secret.key}") String secretKey,
      @Value("${jwt.access-token-validity}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    this.accessTokenValidity = accessTokenValidity;
    this.refreshTokenValidity = refreshTokenValidity;
  }
  // Access Token 생성
  public String generateAccessToken(UserDetails userDetails) {
    return generateToken(userDetails.getUsername(), accessTokenValidity);
  }
  // Refresh Token 생성
  public String generateRefreshToken(UserDetails userDetails) {
    return generateToken(userDetails.getUsername(), refreshTokenValidity);
  }

  private String generateToken(String subject, long validity) {
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + validity))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
  // 유효성 검사
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    }catch (ExpiredJwtException e) {
      log.error("만료된 JWT: {}", token);
    } catch (MalformedJwtException e) {
      log.error("손상된 JWT: {}", token);
    } catch (SignatureException e) {
      log.error("JWT 서명 검증 실패: {}", token);
    } catch (Exception e) {
      log.error("유효하지 않은 JWT: {}", token);
    }

    return false;
  }

  public String getUserEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}