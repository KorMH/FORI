package com.foodri.foodreview.config.naverocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodri.foodreview.config.s3.S3Util;
import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class NaverOcrClient {

  @Value("${naver.ocr.api-url}")
  private String apiUrl;

  @Value("${naver.ocr.secret}")
  private String secretKey;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private final S3Util s3Util;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;


  public NaverOcrClient(RestTemplateBuilder builder, S3Util s3Util, ObjectMapper objectMapper) {
    this.restTemplate = builder.build();
    this.s3Util = s3Util;
    this.objectMapper = objectMapper;
  }

  public boolean isReceipt(String imageUrl) {
    try {
      // 1. S3에서 이미지 다운로드 & Base64 변환
      String base64Image = getBase64Image(imageUrl);

      // 2. OCR API 요청 생성 및 전송
      Map<String, Object> requestPayload = createOcrRequest(base64Image);
      ResponseEntity<Map> response = sendOcrRequest(requestPayload);

      log.info("OCR 응답 데이터: {}", objectMapper.writeValueAsString(response.getBody()));

      // 3. OCR 응답을 분석하여 영수증 여부 확인
      return checkIfReceipt(response.getBody());

    } catch (Exception e) {
      log.error("OCR 처리 실패: {}", e.getMessage(), e);
      throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.OCR_PROCESSING_FAILED);
    }
  }

  private boolean checkIfReceipt(Map<String, Object> response) {
    if (response == null || !response.containsKey("images")) {
      log.warn("OCR 응답이 null이거나 'images' 필드가 없음");
      return false;
    }

    List<Map<String, Object>> images = (List<Map<String, Object>>) response.get("images");
    if (images.isEmpty() || !images.get(0).containsKey("receipt")) {
      log.warn("OCR 응답에서 'receipt' 필드가 없음");
      return false;
    }

    Map<String, Object> receipt = (Map<String, Object>) images.get(0).get("receipt");
    Map<String, Object> result = (Map<String, Object>) receipt.get("result");

    if (result == null) {
      log.warn("OCR 응답에서 'receipt.result' 필드가 없음");
      return false;
    }

    // 가게 정보, 결제 정보, 총액이 모두 존재하는지 확인
    boolean hasStoreInfo = result.containsKey("storeInfo") && result.get("storeInfo") != null;
    boolean hasPaymentInfo = result.containsKey("paymentInfo") && result.get("paymentInfo") != null;
    boolean hasTotalPrice = result.containsKey("totalPrice") && result.get("totalPrice") != null;

    log.info("OCR 분석 결과 - 가게 정보: {}, 결제 정보: {}, 총액 정보: {}", hasStoreInfo, hasPaymentInfo, hasTotalPrice);

    // 필수 정보가 모두 있어야 영수증으로 인정
    return hasStoreInfo && hasPaymentInfo && hasTotalPrice;
  }


  private String getBase64Image(String imageUrl) throws IOException {
    String objectKey = extractS3KeyFromUrl(imageUrl);
    byte[] imageBytes = s3Util.downloadImageFromS3(objectKey);
    return Base64.getEncoder().encodeToString(imageBytes);
  }

  private Map<String, Object> createOcrRequest(String base64Image) {
    Map<String, Object> imageInfo = Map.of(
        "format", "jpg",
        "name", "receipt",
        "data", base64Image
    );

    return Map.of(
        "images", List.of(imageInfo),
        "requestId", UUID.randomUUID().toString(),
        "version", "V2",
        "timestamp", System.currentTimeMillis()
    );
  }

  private ResponseEntity<Map> sendOcrRequest(Map<String, Object> requestPayload) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-OCR-SECRET", secretKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);
    log.info("OCR 요청 JSON: {}", objectMapper.writeValueAsString(requestPayload));

    return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
  }

  private String extractS3KeyFromUrl(String imageUrl) {
    String baseUrl = "https://" + bucketName + ".s3.amazonaws.com/";
    return imageUrl.replace(baseUrl, "");
  }
}

