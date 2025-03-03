package com.epavfra.task.dto;

import java.util.Collection;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalEmailRequestDto {

  @Valid
  private Collection<
      @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format")
          String>
      emailAddresses;
}
