package com.foodri.foodreview.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOW("000_UNKNOW", "알수 없는 에러 발생"),
  USER_NOT_FOUND("001_USER_NOT_FOUND","사용자를 찾을 수 없습니다."),
  RESTAURANT_NOT_FOUND("002_RESTAURANT_NOT_FOUND","식당을 찾을 수 없습니다."),
  RESTAURANT_USER_NOT_FOUND("003_RESTAURANT_USER_NOT_FOUND","식당 주인을 찾을 수 없습니다."),
  RECEIPT_NOT_FOUND("004_RECEIPT_NOT_FOUND","영수증을 찾을 수 없습니다."),
  ACCESS_DENIED("005_ACCESS_DENIED","접근이 거부되었습니다."),
  OCR_INVALID_RESPONSE("006_OCR_INVALID_RESPONSE","응답이 올바르지 않습니다."),
  OCR_NO_TEXT_FOUND("007_OCR_NO_TEXT_FOUND","Text를 찾을 수 없습니다."),
  OCR_PROCESSING_FAILED("008_OCR_PROCESSING_FAILED","OCR 추출에 실패 했습니다."),
  OCR_EXTRACTION_FAILED("009_OCR_EXTRACTION_FAILED","OCR에서 상호명 또는 주소를 추출하지 못했습니다."),
  INVALID_INPUT("010_INVALID_INPUT","해당 파일은 비워있습니다."),
  OCR_RESPONSE_INVALID("011_OCR_RESPONSE_INVALID", "유효하지 않습니다."),
  INVALID_S3_URL("012_INVALID_S3_URL","유효하지  않은 S3 형식입니다."),
  INVALID_RECEIPT("013_INVALID_RECEIPT","영수증 이미지가 아닙니다."),
  RECEIPT_NOT_OWNED("014_RECEIPT_NOT_OWNED", "영수증을 찾을 수 없습니다."),
  REVIEW_ALREADY_EXISTS("015_REVIEW_ALREADY_EXISTS", "이미 존재하는 리뷰 입니다."),
  REVIEW_NOT_FOUND("016_REVIEW_NOT_FOUND","리뷰를 찾을 수 없습니다."),
  UNAUTHORIZED("017_UNAUTHORIZED", "승인이 되지 않았습니다.");
  private final String code;
  private final String msg;
}
