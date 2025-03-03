package com.epavfra.task.controller;

import com.epavfra.task.dto.AdditionalEmailRequestDto;
import com.epavfra.task.dto.AdditionalPhoneNumberDto;
import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.exception.PersonNotFoundException;
import com.epavfra.task.service.PersonService;
import com.epavfra.task.utils.constants.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
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

  @Operation(
      summary = "Get all persons",
      description =
          "Retrieves a list of persons. Optional filters can be applied using query parameters: sex, name, and surname. "
              + "Global exception handling will return a 500 error for unexpected issues.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the list of persons"),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error - handled by global exception handler")
      })
  @GetMapping
  public ResponseEntity<Collection<PersonDto>> getAllPersons(
      @Parameter(description = "Filter by sex", required = false) @RequestParam(required = false)
          final String sex,
      @Parameter(description = "Filter by name", required = false) @RequestParam(required = false)
          final String name,
      @Parameter(description = "Filter by surname", required = false)
          @RequestParam(required = false)
          final String surname) {
    Collection<PersonDto> persons = personService.filterPersons(name, surname, sex);
    return ResponseEntity.ok(persons);
  }

  @Operation(
      summary = "Get person by ID",
      description =
          "Retrieves a single person by their unique ID. "
              + "If the person is not found, a 404 error response is returned via the global exception handler.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the person"),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found - handled by global exception handler"),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error - handled by global exception handler")
      })
  @GetMapping(ApiPaths.GET_PERSON_BY_ID_PATH_SUFFIX)
  public ResponseEntity<PersonDto> getPersonById(
      @Parameter(description = "ID of the person to retrieve", required = true) @PathVariable
          final Long personId) {
    PersonDto personDto = personService.getPersonById(personId);
    return ResponseEntity.ok(personDto);
  }

  @Operation(
      summary = "Create a new person",
      description =
          "Creates a new person with the provided details. "
              + "Validation errors (400) or unexpected errors (500) are managed by the global exception handler.",
      responses = {
        @ApiResponse(responseCode = "201", description = "Successfully created the person"),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed - handled by global exception handler"),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error - handled by global exception handler")
      })
  @PostMapping
  public ResponseEntity<PersonDto> createPerson(
      @Parameter(description = "Person object to create", required = true) @RequestBody @Valid
          final PersonDto personDto) {
    PersonDto createdPerson = personService.createPerson(personDto);
    return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
  }

  @Operation(
      summary = "Delete a person",
      description =
          "Deletes the person with the specified ID. "
              + "If the person is not found, a 404 error response is returned via the global exception handler.",
      responses = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the person"),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found - handled by global exception handler"),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error - handled by global exception handler")
      })
  @DeleteMapping(ApiPaths.DELETE_PERSON_PATH_SUFFIX)
  public ResponseEntity<Void> deletePerson(
      @Parameter(description = "ID of the person to delete", required = true) @PathVariable
          Long personId) {
    personService.deletePerson(personId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Add additional email addresses",
      description =
          "Adds additional email addresses for the specified person and returns the updated person details. "
              + "Validation errors (400), optimistic locking conflicts (409), or unexpected errors (500) are managed by the global exception handler.",
      responses = {
        @ApiResponse(responseCode = "201", description = "Email addresses added successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found - handled by global exception handler"),
        @ApiResponse(
            responseCode = "409",
            description = "Optimistic locking failure - handled by global exception handler"),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error - handled by global exception handler")
      })
  @PostMapping(ApiPaths.ADD_ADDRESSES_PATH_SUFFIX)
  public ResponseEntity<PersonDto> addAdditionalEmailAddresses(
      @Parameter(description = "ID of the person", required = true) @PathVariable Long personId,
      @Parameter(description = "Collection of additional email addresses", required = true)
          @RequestBody
          @Valid
          final AdditionalEmailRequestDto emailAddresses) {
    try {
      PersonDto updatedPerson = personService.addEmailAddresses(personId, emailAddresses);
      return new ResponseEntity<>(updatedPerson, HttpStatus.CREATED);
    } catch (PersonNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(
      summary = "Add additional phone numbers",
      description =
          "Adds additional phone numbers for the specified person and returns the updated person details. "
              + "Validation errors (400), optimistic locking conflicts (409), or unexpected errors (500) are managed by the global exception handler.",
      responses = {
        @ApiResponse(responseCode = "201", description = "Phone numbers added successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found - handled by global exception handler"),
        @ApiResponse(
            responseCode = "409",
            description = "Optimistic locking failure - handled by global exception handler"),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error - handled by global exception handler")
      })
  @PostMapping(ApiPaths.ADD_PHONE_NUMBERS_PATH_SUFFIX)
  public ResponseEntity<PersonDto> addAdditionalPhoneNumbers(
      @Parameter(description = "ID of the person", required = true) @PathVariable Long personId,
      @Parameter(description = "Collection of additional phone numbers", required = true)
          @RequestBody
          @Valid
          final AdditionalPhoneNumberDto phoneNumbers) {
    try {
      PersonDto updatedPerson = personService.addPhoneNumbers(personId, phoneNumbers);
      return new ResponseEntity<>(updatedPerson, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
