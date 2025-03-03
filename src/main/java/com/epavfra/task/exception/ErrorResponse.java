package com.epavfra.task.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private List<FieldError> fieldErrors;

  @Getter
  @Builder
  public static class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;
  }
}
