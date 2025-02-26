package com.epavfra.task.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonNotFoundException extends Exception {

  private String message;
}
