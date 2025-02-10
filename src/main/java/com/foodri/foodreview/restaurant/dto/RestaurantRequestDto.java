package com.foodri.foodreview.restaurant.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequestDto {
  private String name;
  private String address;
  private String phoneNumber;
  private String category;
  private Integer maxCapacity;
  private String openingHours;
  private Integer maxReservationLimit;
  private Integer reservationTimeUnit;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
}
