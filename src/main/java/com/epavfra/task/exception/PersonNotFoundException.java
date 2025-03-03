package com.epavfra.task.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonNotFoundException extends RuntimeException {

  private String message;
}
