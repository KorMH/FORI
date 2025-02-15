package com.foodri.foodreview.like.repository;

import com.foodri.foodreview.like.entity.Like;
import com.foodri.foodreview.review.entity.Review;
import com.foodri.foodreview.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
  long countByReview(Review review);
  Optional<Like> findByReviewAndUser(Review review, User user);
}