package com.foodri.foodreview.user.service;

import com.foodri.foodreview.user.dto.AuthRequestDto;
import com.foodri.foodreview.user.dto.AuthResponseDto;
import com.foodri.foodreview.user.dto.TokenDto;
import com.foodri.foodreview.user.dto.UserRegisterDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  AuthResponseDto register(UserRegisterDto request);
  TokenDto login(AuthRequestDto request);
  void logout(HttpServletRequest request);
}
