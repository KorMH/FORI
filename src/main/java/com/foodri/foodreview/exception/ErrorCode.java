package com.foodri.foodreview.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOW("000_UNKNOW", "알수 없는 에러 발생"),
  USER_NOT_FOUND("001_USER_NOT_FOUND","사용자를 찾을 수 없습니다."),
  RESTAURANT_NOT_FOUND("002_RESTAURANT_NOT_FOUND","식당을 찾을 수 없습니다."),
  RESTAURANT_USER_NOT_FOUND("003_RESTAURANT_USER_NOT_FOUND","식당 주인을 찾을 수 없습니다.");
  private final String code;
  private final String msg;
}
