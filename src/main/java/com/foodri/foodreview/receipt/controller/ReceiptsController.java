package com.foodri.foodreview.receipt.controller;

import com.foodri.foodreview.receipt.dto.ReceiptRequestDto;
import com.foodri.foodreview.receipt.dto.ReceiptResponseDto;
import com.foodri.foodreview.receipt.service.ReceiptService;
import com.foodri.foodreview.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
@Slf4j
public class ReceiptsController {

  private final ReceiptService receiptService;

  /**
   * 영수증 업로드 및 인증
   */
  @PostMapping("/upload")
  public ResponseEntity<ReceiptResponseDto> uploadReceipt(
      @RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    log.info("파일 이름: {}", file.getOriginalFilename());

    ReceiptRequestDto requestDto = ReceiptRequestDto.builder()
        .image(file)
        .build();

    ReceiptResponseDto responseDto = receiptService.uploadReceipt(userDetails.getId(), requestDto);
    return ResponseEntity.ok(responseDto);
  }

  /**
   * 특정 영수증 조회
   */
  @GetMapping("/{receiptId}")
  public ResponseEntity<ReceiptResponseDto> getReceiptById(
      @PathVariable Long receiptId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getId();
    ReceiptResponseDto responseDto = receiptService.getReceiptById(receiptId, userId);
    return ResponseEntity.ok(responseDto);
  }

  /**
   * 사용자의 모든 영수증 조회
   */
  @GetMapping
  public ResponseEntity<Page<ReceiptResponseDto>> getUserReceipts(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Long userId = userDetails.getId();
    Pageable pageable = PageRequest.of(page, size);
    Page<ReceiptResponseDto> responseDto = receiptService.getUserReceipts(userId, pageable);

    return ResponseEntity.ok(responseDto);
  }

  /**
   * 영수증 삭제
   */
  @DeleteMapping("/{receiptId}")
  public ResponseEntity<Void> deleteReceipt(
      @PathVariable Long receiptId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getId();
    receiptService.deleteReceipt(receiptId, userId);
    return ResponseEntity.noContent().build();
  }
}