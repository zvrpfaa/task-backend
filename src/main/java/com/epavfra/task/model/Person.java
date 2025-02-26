package com.epavfra.task.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "surname", nullable = false)
  private String surname;

  @Column(name = "pin", nullable = false, unique = true, length = 11)
  private String pin;

  @Enumerated private Sex sex;

  @ElementCollection
  @CollectionTable(name = "person_phone-numbers", joinColumns = @JoinColumn(name = "person_id"))
  @Column(name = "phone_number")
  private final Set<String> phoneNumbers = new HashSet<>();

  @ElementCollection
  @CollectionTable(name = "person_email_addresses", joinColumns = @JoinColumn(name = "person_id"))
  @Column(name = "email_address")
  private final Set<String> emailAddresses = new HashSet<>();

  @Version private Integer version;

  public static class Builder {
    private final String name;
    private final String surname;
    private final String pin;
    private final Sex sex;
    private final Set<String> phoneNumbers = new HashSet<>();
    private final Set<String> emailAddresses = new HashSet<>();

    public Builder(final String name, final String surname, final String pin, final Sex sex) {
      this.name = name;
      this.surname = surname;
      this.pin = pin;
      this.sex = sex;
    }

    public Builder addPhoneNumbers(Collection<String> phoneNumbers) {
      this.phoneNumbers.addAll(phoneNumbers);
      return this;
    }

    public Builder addEmailAddresses(Collection<String> emailAddresses) {
      this.emailAddresses.addAll(emailAddresses);
      return this;
    }

    public Person build() {
      Person person = new Person();
      person.name = this.name;
      person.surname = this.surname;
      person.pin = this.pin;
      person.sex = this.sex;
      person.phoneNumbers.addAll(this.phoneNumbers);
      person.emailAddresses.addAll(this.emailAddresses);
      return person;
    }
  }
}
