package com.epavfra.task.component;

import com.epavfra.task.model.Sex;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSexConverter implements Converter<String, Sex> {
  @Override
  public Sex convert(String source) {
    if (source.isBlank()) {
      throw new IllegalArgumentException();
    }
    try {
      return Sex.valueOf(source.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid value for Sex: " + source, e);
    }
  }
}
