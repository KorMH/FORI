package com.foodri.foodreview.review.repository;

import com.foodri.foodreview.receipt.entity.Receipts;
import com.foodri.foodreview.review.entity.Review;
import com.foodri.foodreview.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  Optional<Review> findByReceipt(Receipts receipt);
  List<Review> findByUser(User user);
}