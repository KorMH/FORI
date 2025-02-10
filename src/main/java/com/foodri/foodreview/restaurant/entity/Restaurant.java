package com.foodri.foodreview.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodri.foodreview.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@Setter
@AllArgsConstructor
@Table(name = "restaurant")
@Builder(toBuilder = true)
@Entity
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private Integer maxCapacity;

  @Column(nullable = false)
  private String openingHours;

  @Column(nullable = false)
  @JsonProperty("max_reservation_limit")
  private Integer maxReservationLimit;

  @Column(nullable = false)
  private Integer reservationTimeUnit;

  @Column(nullable = false)
  private boolean isOpen;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
