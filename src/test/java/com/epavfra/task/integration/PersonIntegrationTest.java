package com.epavfra.task.integration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epavfra.task.dto.PersonDto;
import com.epavfra.task.model.Person;
import com.epavfra.task.model.Sex;
import com.epavfra.task.repository.PersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private PersonRepository personRepository;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  public void cleanUp() {
    personRepository.deleteAll();
  }

  @Test
  void testFilterBySex() throws Exception {
    addThreePersonsToDatabase();
    MvcResult mvcResult =
        mockMvc
            .perform(get("/api/v1/persons/filter/sex/male"))
            .andExpect(status().isOk())
            .andReturn();
    String contentAsString = mvcResult.getResponse().getContentAsString();
    List<PersonDto> persons = objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertThat(persons.size()).isEqualTo(2);
    boolean allPersonsAreMale =
        persons.stream().map(PersonDto::getSex).allMatch(s -> s == Sex.MALE);
    assertThat(allPersonsAreMale).isTrue();
  }

  @Test
  void testFilterBySurname() throws Exception {
    addThreePersonsToDatabase();
    List<Person> savedPerson = personRepository.findAll();
    assertThat(savedPerson.size()).isEqualTo(3);
    MvcResult mvcResult =
        mockMvc
            .perform(get("/api/v1/persons/filter/surname/smith"))
            .andExpect(status().isOk())
            .andReturn();
    String contentAsString = mvcResult.getResponse().getContentAsString();
    List<PersonDto> persons = objectMapper.readValue(contentAsString, new TypeReference<>() {});
    assertThat(persons.size()).isEqualTo(2);
    boolean allSurnamesAreSmith =
        persons.stream().map(PersonDto::getSurname).allMatch(s -> Objects.equals(s, "Smith"));
    assertThat(allSurnamesAreSmith).isTrue();
  }

  @Test
  void testDeletePerson() throws Exception {
    addPersonToDatabase();
    assertThat(personRepository.findAll()).hasSize(1);
    mockMvc.perform(delete("/api/v1/persons/1")).andExpect(status().isNoContent());
    assertThat(personRepository.findAll().size()).isEqualTo(0);
  }

  private void addPersonToDatabase() throws Exception {
    String personJson =
        """
                {
                    "name": "John",
                    "surname": "Smith",
                    "pin": "12345678901",
                    "sex": "MALE",
                    "emailAddresses": ["johnSmith.gmail.com"],
                    "phoneNumbers": ["+1234567890"]
                }
                """;
    mockMvc
        .perform(
            post("/api/v1/persons/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(personJson))
        .andExpect(status().isCreated());
  }

  private void addThreePersonsToDatabase() {
    Collection<String> persons =
        Stream.of(
                """
            {
                "name": "John",
                "surname": "Smith",
                "pin": "12345678901",
                "sex": "MALE",
                "emailAddresses": ["johnSmith.gmail.com"],
                "phoneNumbers": ["+1234567890"]
            }
            """,
                """
            {
                "name": "Jane",
                "surname": "Smith",
                "pin": "14345678901",
                "sex": "FEMALE",
                "emailAddresses": ["jane.gmail.com"],
                "phoneNumbers": ["+1234567890"]
            }
            """,
                """
            {
                "name": "Michael",
                "surname": "Michaelson",
                "pin": "52345678901",
                "sex": "MALE",
                "emailAddresses": ["michael.gmail.com"],
                "phoneNumbers": ["+52345678440"]
            }
            """)
            .collect(Collectors.toSet());
    persons.forEach(
        p -> {
          try {
            mockMvc
                .perform(
                    post("/api/v1/persons/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(p))
                .andExpect(status().isCreated());
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }
}
