package com.epavfra.task.repository;

import java.util.Collection;

import com.epavfra.task.model.Person;
import com.epavfra.task.model.Sex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

  Collection<Person> findBySex(final Sex sex);

  Collection<Person> findBySurnameContainingIgnoreCase(final String surname);
}
