package com.foodri.foodreview.restaurant.Controller;


import com.foodri.foodreview.restaurant.dto.RestaurantRequestDto;
import com.foodri.foodreview.restaurant.dto.RestaurantResponseDto;
import com.foodri.foodreview.restaurant.entity.Restaurant;
import com.foodri.foodreview.restaurant.service.Impl.RestaurantServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantServiceImpl restaurantService;

  /**
   * 등록
   */
  @PostMapping
  public ResponseEntity<RestaurantResponseDto> createRestaurant(
      @RequestParam Long userId,
      @RequestBody RestaurantRequestDto requestDto){


    RestaurantResponseDto responseDto = restaurantService.createRestaurant(userId,requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  /**
   *  특정 음식점 조회
   */
  @GetMapping("/{restaurantId}")
  public ResponseEntity<RestaurantResponseDto> getRestaurant(@PathVariable Long restaurantId){
    return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
  }

  /**
   * 모든 음식점 조회
   */
  @GetMapping
  public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants() {
    return ResponseEntity.ok(restaurantService.getAllRestaurants());
  }

  /**
   * 음식점 수정
   */
  @PutMapping("/{restaurantId}")
  public ResponseEntity<RestaurantResponseDto> updateRestaurant(
      @PathVariable Long restaurantId,
      @RequestParam Long userId,
      @RequestBody RestaurantRequestDto requestDto
  ){

    return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantId,requestDto,userId));
  }

  /**
   * 음식점 삭제
   */
  @DeleteMapping("/{restaurantId}")
  public ResponseEntity<Void> deleteRestaurant(
      @PathVariable Long restaurantId,
      @RequestParam Long userId
  ) {
    restaurantService.deleteRestaurant(restaurantId,userId);
    return ResponseEntity.noContent().build();
  }



}
