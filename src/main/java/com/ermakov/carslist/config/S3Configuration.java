package com.ermakov.carslist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Configuration(
    String region,
    String bucket,
    String photosFolder,
    String accessKeyId,
    String secretAccessKey
) {
  @Bean
  public S3Client localS3client() {
    return S3Client.builder()
        .credentialsProvider(getAwsCredentialsProvider())
        .region(Region.of(region))
        .build();
  }

  private AwsCredentialsProvider getAwsCredentialsProvider() {
    return StaticCredentialsProvider
        .create(AwsBasicCredentials
            .create(accessKeyId, secretAccessKey));
  }
}