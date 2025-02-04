package com.foodri.foodreview.user.controller;

import com.foodri.foodreview.user.dto.UserDto;
import com.foodri.foodreview.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // 현재 로그인한 사용자 정보 조회
  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(userService.getUserByEmail(userDetails.getUsername()));
  }

  // 특정 사용자 정보 조회
  @GetMapping("/{email}")
  public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  // 사용자 정보 업데이트
  @PutMapping("/update")
  public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal UserDetails userDetails,
      @RequestBody UserDto userDto) {
    return ResponseEntity.ok(userService.updateUser(userDetails.getUsername(), userDto));
  }

  // 사용자 계정 삭제
  @DeleteMapping()
  public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
    userService.deleteUser(userDetails.getUsername());
    return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
  }
}