package com.foodri.foodreview.review.service.Impl;

import com.foodri.foodreview.config.s3.S3Service;
import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import com.foodri.foodreview.receipt.entity.Receipts;
import com.foodri.foodreview.receipt.repository.ReceiptRepository;
import com.foodri.foodreview.review.dto.ReviewDto;
import com.foodri.foodreview.review.dto.ReviewRequestDto;
import com.foodri.foodreview.review.entity.Review;
import com.foodri.foodreview.review.repository.ReviewRepository;
import com.foodri.foodreview.review.service.ReviewService;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReceiptRepository receiptRepository;
  private final UserRepository userRepository;
  private final S3Service s3Service;

  @Override
  public ReviewDto createReview(Long userId, ReviewRequestDto request, MultipartFile imageFile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

    Receipts receipt = receiptRepository.findById(request.getReceiptId())
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.RECEIPT_NOT_FOUND));

    if (!receipt.getUser().equals(user)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.RECEIPT_NOT_OWNED);
    }

    if (reviewRepository.findByReceipt(receipt).isPresent()) {
      throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.REVIEW_ALREADY_EXISTS);
    }

    //S3에 이미지 업로드 후 URL 반환
    String imageUrl = null;
    if (imageFile != null && !imageFile.isEmpty()) {
      imageUrl = s3Service.uploadFile("reviews", imageFile); // "reviews" 폴더에 저장
    }


    //영수증이 인증된 영수증인지 확인
    boolean isReceiptVerified = isReceiptVerified(receipt);

    //인증된 영수증이면 verified = true 설정
    Review review = Review.builder()
        .content(request.getContent())
        .rating(request.getRating())
        .imageUrl(imageUrl)
        .verified(isReceiptVerified)
        .user(user)
        .receipt(receipt)
        .build();

    return ReviewDto.fromEntity(reviewRepository.save(review));
  }

  @Override
  public ReviewDto updateReview(Long userId, Long reviewId, ReviewRequestDto request) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    if (!review.getUser().getId().equals(userId)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.UNAUTHORIZED);
    }

    String updatedImageUrl = request.getImageUrl() != null ? request.getImageUrl() : review.getImageUrl();

    review = review.toBuilder()
        .content(request.getContent())
        .rating(request.getRating())
        .imageUrl(updatedImageUrl)
        .updatedAt(LocalDateTime.now())
        .build();

    return ReviewDto.fromEntity(reviewRepository.save(review));
  }

  @Override
  public void deleteReview(Long userId, Long reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    if (!review.getUser().getId().equals(userId)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.UNAUTHORIZED);
    }

    reviewRepository.delete(review);
  }

  @Override
  public ReviewDto getReview(Long reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    return ReviewDto.fromEntity(review);
  }

  @Override
  public List<ReviewDto> getUserReviews(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

    return reviewRepository.findByUser(user).stream()
        .map(ReviewDto::fromEntity)
        .collect(Collectors.toList());
  }


  private boolean isReceiptVerified(Receipts receipt) {
    return receipt != null;
  }
}
