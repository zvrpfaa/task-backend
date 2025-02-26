package com.epavfra.task.mapper;

import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.model.Person;

public enum PersonMapper implements Mapper<PersonDto, Person>{
  INSTANCE;

  public Person toEntity(final PersonDto personDto) {
    if (personDto == null) return null;

    return new Person.Builder(
            personDto.getName(), personDto.getSurname(), personDto.getPin(), personDto.getSex())
        .addEmailAddresses(personDto.getEmailAddresses())
        .addPhoneNumbers(personDto.getPhoneNumbers())
        .build();
  }

  public PersonDto toDto(final Person person) {
    if (person == null) return null;
    return
        new PersonDto.Builder(
                person.getName(), person.getSurname(), person.getPin(), person.getSex())
            .addEmailAddresses(person.getEmailAddresses())
            .addPhoneNumbers(person.getPhoneNumbers())
            .build();
  }
}
