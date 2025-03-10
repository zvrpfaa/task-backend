package com.epavfra.task.mapper;

import java.util.Collection;

import com.epavfra.task.dto.AdditionalPhoneNumberDto;

public enum PhoneNumberMapper implements Mapper<AdditionalPhoneNumberDto, Collection<String>> {

  INSTANCE;
  @Override
  public AdditionalPhoneNumberDto toDto(Collection<String> entity) {
    return new AdditionalPhoneNumberDto(entity);
  }

  @Override
  public Collection<String> toEntity(AdditionalPhoneNumberDto dto) {
    return dto.getPhoneNumbers();
  }
}
