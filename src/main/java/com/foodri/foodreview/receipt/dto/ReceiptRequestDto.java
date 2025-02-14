package com.foodri.foodreview.receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReceiptRequestDto {
  private MultipartFile image;
}