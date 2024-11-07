package com.ermakov.carslist.service.impl;

import com.ermakov.carslist.config.S3Configuration;
import com.ermakov.carslist.exception.S3DeleteObjectException;
import com.ermakov.carslist.exception.S3PutObjectException;
import com.ermakov.carslist.service.S3Service;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3ServiceImpl implements S3Service {
  private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);
  private static final String FOLDER_PREFIX = "cars";
  private static final String FILE_FORMAT = "%s/%s.%s";

  private final S3Client s3Client;
  private final S3Configuration s3Configuration;

  public S3ServiceImpl(S3Client s3Client, S3Configuration s3Configuration) {
    this.s3Client = s3Client;
    this.s3Configuration = s3Configuration;
  }

  @Override
  public String putFile(String fileName, String fileExtension, byte[] fileBytes) {
    var key = String.format(FILE_FORMAT, FOLDER_PREFIX, fileName, fileExtension);
    var putObjectRequest = PutObjectRequest.builder()
        .key(key)
        .bucket(s3Configuration.bucket())
        .build();
    try {
      s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
      var fileAwsKey = getFileAwsKey(fileName, fileExtension);
      logger.info("New content [{}] was added", fileAwsKey);
      return fileAwsKey;
    } catch (S3Exception e) {
      logger.error("Error during put s3 object with key [{}], message: [{}]",
          putObjectRequest.key(), e.getMessage());
      throw new S3PutObjectException(e.getMessage());
    }
  }

  @Override
  public void deleteFile(String fileName) {
    var deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(s3Configuration.bucket())
        .key(fileName.substring(1))
        .build();
    try {
      s3Client.deleteObject(deleteObjectRequest);
      logger.info("Content [{}] was successfully deleted", fileName);
    } catch (S3Exception e) {
      logger.error("Error during deletion of s3 object with key [{}], message: [{}]",
          deleteObjectRequest.key(), e.getMessage());
      throw new S3DeleteObjectException(e.getMessage());
    }
  }

  private String getFileAwsKey(String fileName, String fileExtension) {
    return UriComponentsBuilder
        .fromPath("/")
        .path(FOLDER_PREFIX)
        .path("/")
        .path(UriUtils.encode(fileName, StandardCharsets.UTF_8))
        .path(".")
        .path(fileExtension)
        .build()
        .toString();
  }
}
