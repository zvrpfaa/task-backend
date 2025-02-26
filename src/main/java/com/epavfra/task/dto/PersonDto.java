package com.epavfra.task.dto;

import com.epavfra.task.model.Sex;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Surname is required")
  private String surname;

  @NotBlank(message = "PIN is required")
  @Size(min = 11, max = 11, message = "PIN must be exactly 11 digits")
  @Pattern(regexp = "\\d{11}", message = "PIN must consist of digits only")
  private String pin;

  @NotNull
  private Sex sex;

  @Valid
  private Collection<
          @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format")
          String>
      emailAddresses = new HashSet<>();

  @Valid
  private Collection<
          @NotBlank(message = "Phone number cannot be blank")
          @Pattern(regexp = "\\+?[0-9]{1,3}-[0-9\\s]{7,15}", message = "Invalid phone number")
          String>
      phoneNumbers = new HashSet<>();

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
    public PersonDto.Builder addPhoneNumbers(Collection<String> phoneNumbers) {
      this.phoneNumbers.addAll(phoneNumbers);
      return this;
    }

    public PersonDto.Builder addEmailAddresses(Collection<String> emailAddresses) {
      this.emailAddresses.addAll(emailAddresses);
      return this;
    }

    public PersonDto build() {
      PersonDto personDto = new PersonDto();
      personDto.name = this.name;
      personDto.surname = this.surname;
      personDto.pin = this.pin;
      personDto.sex = this.sex;
      personDto.phoneNumbers.addAll(this.phoneNumbers);
      personDto.emailAddresses.addAll(this.emailAddresses);
      return personDto;
    }
  }
}
