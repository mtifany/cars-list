package com.ermakov.carslist.service;

public interface S3Service {

  String putFile(String fileName, String fileContent, byte[] fileBytes);
  void deleteFile(String fileName);
}
