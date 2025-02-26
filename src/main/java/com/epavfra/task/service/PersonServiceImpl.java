package com.epavfra.task.service;

import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.exception.PersonNotFoundException;
import com.epavfra.task.mapper.PersonMapper;
import com.epavfra.task.model.Person;
import com.epavfra.task.repository.PersonRepository;
import com.epavfra.task.utils.specification.PersonSpecification;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;

  public PersonServiceImpl(
      final PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  public Collection<PersonDto> getAllPersons() {
    Collection<Person> persons = personRepository.findAll();
    return persons.stream().map(PersonMapper.INSTANCE::toDto).collect(Collectors.toSet());
  }

  @Override
  public Collection<PersonDto> filterPersons(
      final String name, final String surname, final String sex) {
    Specification<Person> spec =
        PersonSpecification.filterByCriteria(name, surname, sex);
    return personRepository.findAll(spec).stream()
        .map(PersonMapper.INSTANCE::toDto)
        .collect(Collectors.toSet());
  }

  @Override
  public PersonDto getPersonById(final Long id) throws PersonNotFoundException {
    return personRepository
        .findById(id)
        .map(PersonMapper.INSTANCE::toDto)
        .orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " was not found."));
  }

  @Override
  public PersonDto createPerson(final PersonDto personDto) {
    Person savedPerson = this.personRepository.save(PersonMapper.INSTANCE.toEntity(personDto));
    return PersonMapper.INSTANCE.toDto(savedPerson);
  }

  @Override
  @Transactional
  @Retryable(
      value = OptimisticLockingFailureException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000))
  public PersonDto addEmailAddresses(final Long id, final Collection<String> emailAddresses)
      throws PersonNotFoundException {
    Person person =
        personRepository
            .findById(id)
            .orElseThrow(
                () -> new PersonNotFoundException("Person with id " + id + " was not found."));
    person.getEmailAddresses().addAll(emailAddresses);
    Person savedPerson = personRepository.save(person);
    return PersonMapper.INSTANCE.toDto(savedPerson);
  }

  @Override
  @Transactional
  @Retryable(
      value = OptimisticLockingFailureException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000))
  public PersonDto addPhoneNumbers(Long id, Collection<String> phoneNumbers)
      throws PersonNotFoundException {
    Person person =
        personRepository
            .findById(id)
            .orElseThrow(
                () -> new PersonNotFoundException("Person with id " + id + " was not found."));
    person.getPhoneNumbers().addAll(phoneNumbers);
    Person savedPerson = personRepository.save(person);
    return PersonMapper.INSTANCE.toDto(savedPerson);
  }

  @Override
  public void deletePerson(Long id) {
    personRepository.deleteById(id);
  }
}
