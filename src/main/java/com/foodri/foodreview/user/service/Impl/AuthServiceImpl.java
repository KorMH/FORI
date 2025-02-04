package com.foodri.foodreview.user.service.Impl;

import com.foodri.foodreview.config.jwt.JwtTokenProvider;
import com.foodri.foodreview.user.dto.AuthRequestDto;
import com.foodri.foodreview.user.dto.AuthResponseDto;
import com.foodri.foodreview.user.dto.TokenDto;
import com.foodri.foodreview.user.dto.UserRegisterDto;
import com.foodri.foodreview.user.entity.Role;
import com.foodri.foodreview.user.entity.Token;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.TokenRepository;
import com.foodri.foodreview.user.repository.UserRepository;
import com.foodri.foodreview.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;
  private final TokenRepository tokenRepository;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  @Transactional
  public AuthResponseDto register(UserRegisterDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("이미 사용 중인 이메일입니다.");
    }

    boolean isOwner = Boolean.TRUE.equals(request.isOwner());
    if(isOwner && (request.getRestaurantName() == null || request.getRestaurantName().trim().isEmpty())){
      throw new RuntimeException("레스토랑 이름을 입력해주세요.");
    }

    User user = User.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .phoneNumber(request.getPhoneNumber())
        .role(isOwner ? Role.ROLE_OWNER : Role.ROLE_USER)
        .restaurantName(isOwner ? request.getRestaurantName() : null)
        .build();
    userRepository.save(user);
    return new AuthResponseDto("회원가입이 완료되었습니다.");
  }

  @Override
  public TokenDto login(AuthRequestDto request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());

    String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

    Token token = new Token(user, user.getEmail(), refreshToken);
    tokenRepository.save(token);

    return new TokenDto(accessToken, refreshToken);
  }

  @Override
  @Transactional
  public void logout(HttpServletRequest request) {
    // 요청 헤더에서 Authorization (Bearer 토큰) 추출
    String token = jwtTokenProvider.resolveToken(request);

    // 토큰이 없으면 예외 처리
    if (token == null || !jwtTokenProvider.validateToken(token)) {
      throw new RuntimeException("유효하지 않은 토큰입니다.");
    }

    // 토큰에서 사용자 이메일 추출
    String email = jwtTokenProvider.getUserEmail(token);
    int deletedCount = tokenRepository.deleteByEmail(email);
    if (deletedCount == 0) {
      throw new RuntimeException("이미 로그아웃 된 사용자 입니다.");
    }
  }
}
