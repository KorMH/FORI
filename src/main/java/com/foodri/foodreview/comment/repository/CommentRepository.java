package com.foodri.foodreview.comment.repository;

import com.foodri.foodreview.comment.entity.Comment;
import com.foodri.foodreview.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByReview(Review review);
}