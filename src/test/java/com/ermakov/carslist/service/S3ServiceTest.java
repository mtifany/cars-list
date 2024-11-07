package com.ermakov.carslist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ermakov.carslist.config.S3Configuration;
import com.ermakov.carslist.exception.S3DeleteObjectException;
import com.ermakov.carslist.exception.S3PutObjectException;
import com.ermakov.carslist.service.impl.S3ServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

  private static final String FILE_NAME = "photo";
  private static final String FILE_EXTENSION = "jpg";

  @Mock
  private S3Client s3Client;
  @Mock
  private S3Configuration s3Configuration;
  @InjectMocks
  private S3ServiceImpl s3Service;

  @Test
  void whenPutContent_thenSuccess() {
    String response = s3Service.putFile(FILE_NAME, FILE_EXTENSION, new byte[1]);

    assertEquals("/cars/photo.jpg", response);
    verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  void whenPutContent_thenS3PutObjectExceptionThrown() {
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenThrow(
        S3Exception.class);

    assertThrows(S3PutObjectException.class,
        () -> s3Service.putFile(FILE_NAME, FILE_EXTENSION, new byte[1]));
  }

  @Test
  void whenDeleteContent_thenSuccess() {
    s3Service.deleteFile(FILE_NAME);

    verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
  }

  @Test
  void whenDeleteContent_thenS3DeleteObjectExceptionThrown() {
    when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenThrow(S3Exception.class);

    assertThrows(S3DeleteObjectException.class, () -> s3Service.deleteFile(FILE_NAME));
  }

}
