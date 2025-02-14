package com.foodri.foodreview.config.s3;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  @Value("${cloud.aws.region.static}")
  private String region;

  /**
   * 파일 업로드 (폴더 지정 가능)
   * @param folderName S3 내 폴더명
   * @param file 업로드할 파일
   * @return S3 URL 반환
   */
  public String uploadFile(String folderName, MultipartFile file) {
    if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
      throw new IllegalArgumentException("업로드할 파일이 존재하지 않습니다.");
    }

    //  UUID + ASCII-only 파일명으로 변경 (한글 제거)
    String originalFileName = file.getOriginalFilename();
    String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
    String asciiFileName = UUID.randomUUID().toString() + extension; // UUID + 확장자 유지

    //  S3에 저장할 경로
    String s3Key = folderName + "/" + asciiFileName;

    try {
      // S3에 업로드 요청
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

      // URL Encoding 적용
      String encodedFileName = URLEncoder.encode(asciiFileName, StandardCharsets.UTF_8);
      String imageUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + folderName + "/" + encodedFileName;

      System.out.println("S3 업로드 완료! URL: " + imageUrl);
      return imageUrl;
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
    }
  }

  /**
   * S3 파일 삭제
   * @param folderName 폴더명 (예: "receipts", "reviews")
   * @param fileName 파일명
   */
  public void deleteFile(String folderName, String fileName) {
    String s3Key = folderName + "/" + fileName;

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    s3Client.deleteObject(deleteObjectRequest);
  }

  /**
   * S3 URL에서 파일 키(폴더/파일명)만 추출하는 유틸리티 메서드
   */
  public String extractS3KeyFromUrl(String imageUrl) {
    String baseUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
    return imageUrl.replace(baseUrl, ""); // URL에서 S3 키(폴더/파일명) 부분만 추출
  }

  public String getBucketName() {
    return bucketName;
  }
}
