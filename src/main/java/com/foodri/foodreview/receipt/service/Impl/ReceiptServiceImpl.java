package com.foodri.foodreview.receipt.service.Impl;

import com.foodri.foodreview.config.naverocr.NaverOcrClient;
import com.foodri.foodreview.config.s3.S3Service;
import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import com.foodri.foodreview.receipt.dto.ReceiptRequestDto;
import com.foodri.foodreview.receipt.dto.ReceiptResponseDto;
import com.foodri.foodreview.receipt.entity.Receipts;
import com.foodri.foodreview.receipt.repository.ReceiptRepository;
import com.foodri.foodreview.receipt.service.ReceiptService;
import com.foodri.foodreview.restaurant.repository.RestaurantRepository;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
  private final ReceiptRepository receiptRepository;
  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final S3Service s3Service;
  private final NaverOcrClient naverOcrClient;

  @Transactional
  @Override
  public ReceiptResponseDto uploadReceipt(Long userId, ReceiptRequestDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND));

    String imageUrl = s3Service.uploadFile("Receipts", requestDto.getImage());

    // OCR을 통해 영수증 여부 확인
    boolean isValidReceipt;
    try {
      isValidReceipt = naverOcrClient.isReceipt(imageUrl);
    } catch (Exception e) {
      log.error("OCR 분석 중 오류 발생: {}", e.getMessage(), e);
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.OCR_PROCESSING_FAILED);
    }

    if (!isValidReceipt) {
      log.warn("업로드된 이미지가 영수증이 아님");
      throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_RECEIPT);
    }

    Receipts receipt = Receipts.builder()
        .imageUrl(imageUrl)
        .user(user)
        .build();

    return new ReceiptResponseDto(receiptRepository.save(receipt));
  }
  /**
   * 특정 영수증 조회
   */
  @Transactional(readOnly = true)
  @Override
  public ReceiptResponseDto getReceiptById(Long receiptId, Long userId) {
    Receipts receipt = receiptRepository.findById(receiptId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.RECEIPT_NOT_FOUND));

    if (!receipt.getUser().getId().equals(userId)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
    }

    return new ReceiptResponseDto(receipt);
  }



  /**
   * 사용자의 모든 영수증 조회 (페이징)
   */
  @Transactional(readOnly = true)
  @Override
  public Page<ReceiptResponseDto> getUserReceipts(Long userId, Pageable pageable) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND));

    return receiptRepository.findByUser(user, pageable)
        .map(ReceiptResponseDto::new);
  }

  @Transactional
  @Override
  public void deleteReceipt(Long receiptId, Long userId) {
    Receipts receipt = receiptRepository.findById(receiptId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.RECEIPT_NOT_FOUND));

    if (!receipt.getUser().getId().equals(userId)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
    }

    String s3Key = s3Service.extractS3KeyFromUrl(receipt.getImageUrl());
    s3Service.deleteFile(s3Service.getBucketName(),s3Key);

    receiptRepository.delete(receipt);
  }
}
