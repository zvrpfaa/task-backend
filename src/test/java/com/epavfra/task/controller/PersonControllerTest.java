package com.epavfra.task.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.model.Person;
import com.epavfra.task.model.Sex;
import com.epavfra.task.service.PersonService;
import com.epavfra.task.utils.constants.ApiPaths;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PersonService personService;

  @Test
  void testCreatePerson() throws Exception {
    PersonDto personDto =
        new PersonDto.Builder("John", "Smith", "11223344556", Sex.MALE)
            .addPhoneNumbers(Set.of("+1-5556454222", "+212-31455353"))
            .addEmailAddresses(Set.of("johnsmith@gmail.com"))
            .build();
    when(personService.createPerson(any(PersonDto.class))).thenReturn(personDto);
    this.mockMvc.perform(
        post(ApiPaths.PERSONS_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("John")))
        .andExpect(jsonPath("$.surname", is("Smith")))
        .andExpect(jsonPath("$.pin", is("11223344556")));
  }
}
