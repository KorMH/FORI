package com.foodri.foodreview.restaurant.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponseDto {
  private Long id;
  private String name;
  private String address;
  private String phoneNumber;
  private String category;
  private Integer maxCapacity;
  private String openingHours;
  private Integer maxReservationLimit;
  private Integer reservationTimeUnit;
  private boolean isOpen;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long userId; // 사업자 ID
}
