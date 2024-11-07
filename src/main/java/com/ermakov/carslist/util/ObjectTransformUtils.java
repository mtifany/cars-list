package com.ermakov.carslist.util;

import com.ermakov.carslist.exception.BadRequestException;
import com.ermakov.carslist.exception.PhotoProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class ObjectTransformUtils {
  private ObjectTransformUtils() {
  }

  public static  <T> T getObjectFromJson(String json, Class<T> clazz) {
    var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new BadRequestException();
    }
  }

  public static byte[] getPhotoBytes(MultipartFile photo){
    try {
      return photo.getBytes();
    } catch (IOException e) {
      throw new PhotoProcessingException();
    }
  }
}
