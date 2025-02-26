package com.epavfra.task.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.epavfra.task.model.Sex;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonDtoTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  void testValidPerson() {
    PersonDto personDto = new PersonDto.Builder(
        "John", "Smith", "11223344551", Sex.MALE).build();
    Set<ConstraintViolation<PersonDto>> violations = validator.validate(personDto);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testBlankName() {
    PersonDto personDto = new PersonDto.Builder(
        "", "Smith", "11223344551", Sex.MALE).build();
    Set<ConstraintViolation<PersonDto>> violations = validator.validate(personDto);
    assertFalse(violations.isEmpty(), "Name is required");
  }
}
