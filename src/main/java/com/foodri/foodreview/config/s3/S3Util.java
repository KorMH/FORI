package com.foodri.foodreview.config.s3;

import java.io.IOException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
public class S3Util {

  private final S3Presigner s3Presigner;
  private final S3Client s3Client;
  private String bucketName;
  private String baseUrl;

  public S3Util(S3Presigner s3Presigner, S3Client s3Client,
      @Value("${cloud.aws.s3.bucket}") String bucketName,
      @Value("${cloud.aws.s3.base-url}") String baseUrl) {
    this.s3Presigner = s3Presigner;
    this.s3Client = s3Client;
    this.bucketName = bucketName;
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
  }

  public String generatePresignedUrl(String imageUrl) {
    String objectKey = extractS3KeyFromUrl(imageUrl);

    // Presigned URL 생성 (유효기간 10분)
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10)) // 10분 후 만료
            .getObjectRequest(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .responseContentType("image/jpeg")
                .build())
            .build()
    );

    return presignedRequest.url().toString();
  }

  public byte[] downloadImageFromS3(String imageUrl) throws IOException {
    String objectKey = extractS3KeyFromUrl(imageUrl);

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

    ResponseBytes<?> responseBytes = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());
    return responseBytes.asByteArray();
  }

  // S3 URL에서 파일 경로(오브젝트 키) 추출
  private String extractS3KeyFromUrl(String imageUrl) {
    if (!imageUrl.startsWith(baseUrl)) {
      throw new IllegalArgumentException("Invalid S3 URL: " + imageUrl);
    }
    return imageUrl.substring(baseUrl.length());
  }
}