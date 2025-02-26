package com.epavfra.task.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdditionalPhoneNumberDto {

  @Valid
  private Collection<
      @NotBlank(message = "Phone number cannot be blank")
      @Pattern(regexp = "\\+?[0-9]{1,3}-[0-9\\s]{7,15}", message = "Invalid phone number")
          String>
      phoneNumbers;
}
