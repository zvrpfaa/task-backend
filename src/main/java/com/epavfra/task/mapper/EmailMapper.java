package com.epavfra.task.mapper;

import java.util.Collection;
import java.util.Map;

import com.epavfra.task.dto.AdditionalEmailRequestDto;

public enum EmailMapper implements Mapper<AdditionalEmailRequestDto, Collection<String>> {

  INSTANCE;
  @Override
  public AdditionalEmailRequestDto toDto(final Collection<String> entity) {
    return new AdditionalEmailRequestDto(entity);
  }

  @Override
  public Collection<String> toEntity(final AdditionalEmailRequestDto dto) {
    return dto.getEmailAddresses();
  }
}
