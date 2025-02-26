package com.epavfra.task.mapper;

public interface Mapper<T, R> {

  T toDto(R entity);

  R toEntity(T dto);
}
