package com.foodri.foodreview.user.controller;

import com.foodri.foodreview.user.dto.AuthRequestDto;
import com.foodri.foodreview.user.dto.AuthResponseDto;
import com.foodri.foodreview.user.dto.TokenDto;
import com.foodri.foodreview.user.dto.UserRegisterDto;
import com.foodri.foodreview.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // 회원가입
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody UserRegisterDto request) {
    authService.register(request);
    return ResponseEntity.ok("회원가입 성공");
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody AuthRequestDto request) {
    return ResponseEntity.ok(authService.login(request));
  }

  // 로그아웃
  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    authService.logout(request);
    return ResponseEntity.ok("로그아웃 성공");
  }
}