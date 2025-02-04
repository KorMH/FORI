package com.foodri.foodreview.user.service.Impl;

import com.foodri.foodreview.user.dto.UserDto;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.UserRepository;
import com.foodri.foodreview.user.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDto getUserByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new RuntimeException("사용자를 찾을 수 없습니다."));
    return UserDto.builder()
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .role(user.getRole())
        .restaurantName(user.getRestaurantName())
        .build();
  }

  //회원 정보 수정 (updateUser)
  @Override
  @Transactional
  public UserDto updateUser(String email, UserDto userDto) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    // 전화번호 업데이트
    user = user.toBuilder()
        .phoneNumber(userDto.getPhoneNumber())
        .restaurantName(user.getRole().name().equals("OWNER") ? userDto.getRestaurantName() : null)
        .build();

    userRepository.save(user);

    return UserDto.builder()
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .role(user.getRole())
        .restaurantName(user.getRestaurantName())
        .build();
  }

  //회원 탈퇴 (deleteUser)
  @Override
  @Transactional
  public void deleteUser(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    userRepository.delete(user);
  }
}

