package com.epavfra.task.repository;

import com.epavfra.task.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonRepository
    extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {}
