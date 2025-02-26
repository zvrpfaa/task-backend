package com.epavfra.task.controller;

import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.exception.PersonNotFoundException;
import com.epavfra.task.model.Sex;
import com.epavfra.task.service.PersonService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/v1/persons")
public class PersonController {

  private final PersonService personService;

  public PersonController(final PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/all")
  public Collection<PersonDto> getAllPersons() {
    return personService.getAllPersons();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonDto> getPersonById(final @PathVariable Long id) {
    try {
      PersonDto personDto = personService.getPersonById(id);
      return ResponseEntity.ok(personDto);
    } catch (PersonNotFoundException e) {
      log.warn(e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/create")
  public ResponseEntity<PersonDto> createPerson(final @RequestBody PersonDto personDto) {
    PersonDto createdPerson = personService.createPerson(personDto);
    return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
    personService.deletePerson(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/add_addresses")
  public ResponseEntity<PersonDto> addAdditionalEmailAddresses(
      final @PathVariable Long id, final @RequestBody Collection<String> emailAddresses) {
    try {
      return new ResponseEntity<>(
          personService.addEmailAddresses(id, emailAddresses), HttpStatus.CREATED);
    } catch (PersonNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/{id}/add_phone_numbers")
  public ResponseEntity<PersonDto> addAdditionalPhoneNumbers(
      final @PathVariable Long id, final @RequestBody Collection<String> phoneNumbers) {
    try {
      return new ResponseEntity<>(
          personService.addPhoneNumbers(id, phoneNumbers), HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/filter/sex/{sex}")
  public ResponseEntity<Collection<PersonDto>> filterPersonsBySex(final @PathVariable Sex sex) {
    try {
      return ResponseEntity.ok(personService.filterBySex(sex));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }

  @GetMapping("/filter/surname/{surname}")
  public ResponseEntity<Collection<PersonDto>> filterPersonsBySurname(
      final @PathVariable String surname) {
    try {
      return ResponseEntity.ok(personService.filterBySurname(surname));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.internalServerError().build();
    }
  }
}
