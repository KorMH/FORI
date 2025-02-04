package com.foodri.foodreview.user.dto;

import com.foodri.foodreview.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private String email;
  private String phoneNumber;
  private Role role;
  private String restaurantName;

}
