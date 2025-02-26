package com.epavfra.task.controller;

import com.epavfra.task.utils.constants.ApiPaths;
import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.exception.PersonNotFoundException;
import com.epavfra.task.model.Sex;
import com.epavfra.task.service.PersonService;
import java.util.Collection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(ApiPaths.PERSONS_PATH)
@Tag(name = "Person API", description = "Operations pertaining to persons")
public class PersonController {

  private final PersonService personService;

  public PersonController(final PersonService personService) {
    this.personService = personService;
  }

  @GetMapping
  @Operation(
      summary = "Get all persons",
      description = "Retrieves a list of persons. Optional filters can be applied using the query parameters: sex, name, and surname.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of persons"),

      }
  )
  public ResponseEntity<Collection<PersonDto>> getAllPersons(
      @RequestParam(required = false) final String sex,
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final String surname) {
    Collection<PersonDto> persons = personService.filterPersons(name, surname, sex);
    return ResponseEntity.ok(persons);
  }

  @GetMapping(ApiPaths.GET_PERSON_BY_ID_PATH_SUFFIX)
  public ResponseEntity<PersonDto> getPersonById(@PathVariable final Long personId) {
    PersonDto personDto = personService.getPersonById(personId);
    return ResponseEntity.ok(personDto);
  }

  @PostMapping
  public ResponseEntity<PersonDto> createPerson(@RequestBody @Valid final PersonDto personDto) {
    PersonDto createdPerson = personService.createPerson(personDto);
    return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
  }

  @DeleteMapping(ApiPaths.DELETE_PERSON_PATH_SUFFIX)
  public ResponseEntity<Void> deletePerson(@PathVariable Long personId) {
    personService.deletePerson(personId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(ApiPaths.ADD_ADDRESSES_PATH_SUFFIX)
  public ResponseEntity<PersonDto> addAdditionalEmailAddresses(
      final @PathVariable Long personId, final @RequestBody Collection<String> emailAddresses) {
    try {
      return new ResponseEntity<>(
          personService.addEmailAddresses(personId, emailAddresses), HttpStatus.CREATED);
    } catch (PersonNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping(ApiPaths.ADD_PHONE_NUMBERS_PATH_SUFFIX)
  public ResponseEntity<PersonDto> addAdditionalPhoneNumbers(
      final @PathVariable Long personId, final @RequestBody Collection<String> phoneNumbers) {
    try {
      return new ResponseEntity<>(
          personService.addPhoneNumbers(personId, phoneNumbers), HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
