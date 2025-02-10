package com.foodri.foodreview.restaurant.repository;

import com.foodri.foodreview.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
