package com.foodri.foodreview.restaurant.service.Impl;

import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import com.foodri.foodreview.restaurant.dto.RestaurantRequestDto;
import com.foodri.foodreview.restaurant.dto.RestaurantResponseDto;
import com.foodri.foodreview.restaurant.entity.Restaurant;
import com.foodri.foodreview.restaurant.repository.RestaurantRepository;
import com.foodri.foodreview.restaurant.service.RestaurantService;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;

  /**
   * 음식점 등록
   */
  @Override
  @Transactional
  public RestaurantResponseDto createRestaurant(Long userId, RestaurantRequestDto requestDto){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST,ErrorCode.USER_NOT_FOUND));

    Restaurant restaurant = Restaurant.builder()
        .name(requestDto.getName())
        .address(requestDto.getAddress())
        .phoneNumber(requestDto.getPhoneNumber())
        .category(requestDto.getCategory())
        .openingHours(requestDto.getOpeningHours())
        .maxCapacity(requestDto.getMaxCapacity())
        .maxReservationLimit(requestDto.getMaxReservationLimit())
        .reservationTimeUnit(requestDto.getReservationTimeUnit())
        .isOpen(true)
        .user(user)
        .createdAt(requestDto.getCreateAt())
        .updatedAt(requestDto.getUpdateAt())
        .build();

     restaurantRepository.save(restaurant);
     return changeToDto(restaurant);
  }

  /**
   * 특정 음식점 조회
   */
  @Override
  @Transactional(readOnly = true)
  public RestaurantResponseDto getRestaurantById(Long restaurantId) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST,ErrorCode.RESTAURANT_NOT_FOUND));
    return changeToDto(restaurant);
  }

  /**
   *모든 음식점 조회
   */
  @Override
  @Transactional(readOnly = true)
  public List<RestaurantResponseDto> getAllRestaurants() {
    return restaurantRepository.findAll()
        .stream()
        .map(this::changeToDto)
        .collect(Collectors.toList());
  }

  /**
   *  음식점 수정
   */
  @Override
  public RestaurantResponseDto updateRestaurant(Long restaurantId, RestaurantRequestDto requestDto,
      Long userId) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST,ErrorCode.RESTAURANT_NOT_FOUND));

    if(!restaurant.getUser().getId().equals(userId)){
      throw new CustomException(HttpStatus.BAD_REQUEST,ErrorCode.RESTAURANT_USER_NOT_FOUND);
    }

    restaurant = restaurant.toBuilder()
        .name(requestDto.getName())
        .address(requestDto.getAddress())
        .phoneNumber(requestDto.getPhoneNumber())
        .category(requestDto.getCategory())
        .maxCapacity(requestDto.getMaxCapacity())
        .openingHours(requestDto.getOpeningHours())
        .maxReservationLimit(requestDto.getMaxReservationLimit())
        .reservationTimeUnit(requestDto.getReservationTimeUnit())
        .build();

    restaurantRepository.save(restaurant);
    return changeToDto(restaurant);
  }

  @Override
  @Transactional
  public void deleteRestaurant(Long restaurantId, Long userId) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST,ErrorCode.RESTAURANT_NOT_FOUND));

    if(!restaurant.getUser().getId().equals(userId)){
      throw new CustomException(HttpStatus.BAD_REQUEST,ErrorCode.RESTAURANT_USER_NOT_FOUND);
    }

    restaurantRepository.delete(restaurant);
  }

  private RestaurantResponseDto changeToDto(Restaurant restaurant){
    return RestaurantResponseDto.builder()
        .id(restaurant.getId())
        .name(restaurant.getName())
        .address(restaurant.getAddress())
        .phoneNumber(restaurant.getPhoneNumber())
        .category(restaurant.getCategory())
        .maxCapacity(restaurant.getMaxCapacity())
        .openingHours(restaurant.getOpeningHours())
        .maxReservationLimit(restaurant.getMaxReservationLimit())
        .reservationTimeUnit(restaurant.getReservationTimeUnit())
        .isOpen(restaurant.isOpen())
        .createdAt(restaurant.getCreatedAt())
        .updatedAt(restaurant.getUpdatedAt())
        .userId(restaurant.getUser().getId())
        .build();
  }
}
