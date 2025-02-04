package com.foodri.foodreview.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtTokenConfig {

  @Bean
  public JwtTokenProvider jwtTokenProvider(
      @Value("${jwt.secret.key}") String secretKey,
      @Value("${jwt.access-token-validity}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
    return new JwtTokenProvider(secretKey, accessTokenValidity, refreshTokenValidity);
  }
}