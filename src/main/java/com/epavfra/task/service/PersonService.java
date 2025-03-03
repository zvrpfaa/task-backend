package com.epavfra.task.service;

import java.util.Collection;

import com.epavfra.task.dto.AdditionalEmailRequestDto;
import com.epavfra.task.dto.AdditionalPhoneNumberDto;
import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.exception.PersonNotFoundException;
import com.epavfra.task.model.Person;
import com.epavfra.task.model.Sex;
import jakarta.transaction.Transactional;

public interface PersonService {
  Collection<PersonDto> getAllPersons();

  Collection<PersonDto> filterPersons(final String name, final String surname, final String sex);

  PersonDto getPersonById(final Long id) throws PersonNotFoundException;

  PersonDto createPerson(final PersonDto personDto);

  PersonDto addEmailAddresses(final Long id, final AdditionalEmailRequestDto emailAddresses)
      throws PersonNotFoundException;

  PersonDto addPhoneNumbers(final Long id, final AdditionalPhoneNumberDto phoneNumbers)
      throws PersonNotFoundException;

  void deletePerson(Long id);
}
