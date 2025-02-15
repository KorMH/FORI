package com.foodri.foodreview.receipt.dto;

import com.foodri.foodreview.receipt.entity.Receipts;
import lombok.Getter;

@Getter
public class ReceiptResponseDto {
  private Long id;
  private String imageUrl;
  private Long userId;


  public ReceiptResponseDto(Receipts receipts){
    this.id = receipts.getId();
    this.imageUrl = receipts.getImageUrl();
    this.userId = receipts.getUser().getId();
  }
}
