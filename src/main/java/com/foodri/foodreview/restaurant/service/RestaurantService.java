package com.foodri.foodreview.restaurant.service;

import com.foodri.foodreview.restaurant.dto.RestaurantRequestDto;
import com.foodri.foodreview.restaurant.dto.RestaurantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
  RestaurantResponseDto createRestaurant(Long userId, RestaurantRequestDto requestDto);
  RestaurantResponseDto getRestaurantById(Long restaurantId);
  RestaurantResponseDto updateRestaurant(Long restaurantId, RestaurantRequestDto requestDto, Long userId);
  Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable);
  void deleteRestaurant(Long restaurantId, Long userId);
}
