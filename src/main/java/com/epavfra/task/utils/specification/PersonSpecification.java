package com.epavfra.task.utils.specification;

import java.util.HashSet;
import java.util.Set;

import com.epavfra.task.model.Person;
import com.epavfra.task.model.Sex;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

  public static Specification<Person> filterByCriteria(
      final String name, final String surname, final String sex) {
    return (root, query, criteriaBuilder) -> {
      Set<Predicate> predicates = new HashSet<>();
      if (name != null) {
        predicates.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
      }
      if (surname != null) {
        predicates.add(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%"));
      }
      if (sex != null) {
        Sex sexEnum = convertSexStringToEnum(sex);
        predicates.add(criteriaBuilder.equal(root.get("sex"), sexEnum));
      }
      return predicates.isEmpty() ? criteriaBuilder.conjunction()
          : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  private static Sex convertSexStringToEnum(String source) {
    if (source.isBlank()) {
      throw new IllegalArgumentException();
    }
    try {
      return Sex.valueOf(source.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid value for Sex: " + source, e);
    }
  }
}
