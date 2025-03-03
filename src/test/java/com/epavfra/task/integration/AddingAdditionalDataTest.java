package com.epavfra.task.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epavfra.task.model.Person;
import com.epavfra.task.repository.PersonRepository;
import com.epavfra.task.utils.constants.ApiPaths;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddingAdditionalDataTest {

  @Autowired private PersonRepository personRepository;
  @Autowired private MockMvc mockMvc;
  @Autowired private EntityManagerFactory entityManagerFactory;

  @BeforeEach
  public void cleanUp() {
    personRepository.deleteAll();
  }

  @Test
  void addAdditionalEmailsTest() throws Exception {
    addPersonToDatabase();
    Long personId = 1L;
    String url =
        UriComponentsBuilder.fromUriString(ApiPaths.ADD_ADDRESSES_PATH)
            .buildAndExpand(personId)
            .toUriString();
    mockMvc
        .perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
    {
    "emailAddresses": [
      "john1@example.com",
      "john2@example.com",
      "john3@example.com"
      ]
      }
    """))
        .andExpect(status().isCreated());
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      Person updatedPerson =
          em.createQuery(
                  "SELECT p FROM Person p LEFT JOIN FETCH p.emailAddresses WHERE p.id = :id",
                  Person.class)
              .setParameter("id", 1L)
              .getSingleResult();
      assertThat(updatedPerson.getEmailAddresses())
          .containsExactlyInAnyOrder(
              "john1@example.com", "john2@example.com", "john3@example.com", "johnSmith@gmail.com");
      tx.commit();
    } catch (Exception ex) {
      tx.rollback();
      throw ex;
    } finally {
      em.close();
    }
  }

  @Test
  void addAdditionalPhoneNumbers() throws Exception {
    addPersonToDatabase();
    Long personId = 1L;
    String url =
        UriComponentsBuilder.fromUriString(ApiPaths.ADD_PHONE_NUMBERS_PATH)
            .buildAndExpand(personId)
            .toUriString();
    mockMvc
        .perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
      {
      "phoneNumbers": [
      "+122-2234567",
      "+152-2234533"
      ]
      }
    """))
        .andExpect(status().isCreated());
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      Person updatedPerson =
          em.createQuery(
                  "SELECT p FROM Person p LEFT JOIN FETCH p.phoneNumbers WHERE p.id = :id",
                  Person.class)
              .setParameter("id", 1L)
              .getSingleResult();
      assertThat(updatedPerson.getPhoneNumbers())
          .containsExactlyInAnyOrder("+122-2234567", "+152-2234533", "+123-45627890");
      tx.commit();
    } catch (Exception ex) {
      tx.rollback();
      throw ex;
    } finally {
      em.close();
    }
  }

  private void addPersonToDatabase() throws Exception {
    String personJson =
        """
                {
                    "name": "John",
                    "surname": "Smith",
                    "pin": "12345678901",
                    "sex": "MALE",
                    "emailAddresses": ["johnSmith@gmail.com"],
                    "phoneNumbers": ["+123-45627890"]
                }
        """;
    mockMvc
        .perform(
            post(ApiPaths.PERSONS_PATH).contentType(MediaType.APPLICATION_JSON).content(personJson))
        .andExpect(status().isCreated());
  }
}
