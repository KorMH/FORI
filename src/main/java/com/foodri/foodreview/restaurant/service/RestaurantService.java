package com.foodri.foodreview.restaurant.service;

import com.foodri.foodreview.restaurant.dto.RestaurantRequestDto;
import com.foodri.foodreview.restaurant.dto.RestaurantResponseDto;
import java.util.List;

public interface RestaurantService {
  RestaurantResponseDto createRestaurant(Long userId, RestaurantRequestDto requestDto);
  RestaurantResponseDto getRestaurantById(Long restaurantId);
  RestaurantResponseDto updateRestaurant(Long restaurantId, RestaurantRequestDto requestDto, Long userId);
  List<RestaurantResponseDto> getAllRestaurants();
  void deleteRestaurant(Long restaurantId, Long userId);
}
