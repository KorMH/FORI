package com.foodri.foodreview.user.service;

import com.foodri.foodreview.user.dto.UserDto;

public interface UserService {
  UserDto getUserByEmail(String email);
  UserDto updateUser(String email, UserDto userDto);
  void deleteUser(String email);
}
