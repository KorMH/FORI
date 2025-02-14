package com.foodri.foodreview.receipt.service;

import com.foodri.foodreview.receipt.dto.ReceiptRequestDto;
import com.foodri.foodreview.receipt.dto.ReceiptResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceiptService {
  ReceiptResponseDto uploadReceipt(Long userId, ReceiptRequestDto requestDto);
  Page<ReceiptResponseDto> getUserReceipts(Long userId, Pageable pageable);
  ReceiptResponseDto getReceiptById(Long receiptId, Long userId);
  void deleteReceipt(Long receiptId, Long userId);
}