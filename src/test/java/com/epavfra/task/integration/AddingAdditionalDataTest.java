package com.epavfra.task.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epavfra.task.model.Person;
import com.epavfra.task.repository.PersonRepository;
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
    mockMvc
        .perform(
            post("/api/v1/persons/1/add_addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
      [
      "john1@example.com",
      "john2@example.com",
      "john3@example.com"
      ]
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
              "john1@example.com", "john2@example.com", "john3@example.com", "johnSmith.gmail.com");
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
    mockMvc
        .perform(
            post("/api/v1/persons/1/add_phone_numbers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
      [
      "+1222234567",
      "+1522234533"
      ]
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
          .containsExactlyInAnyOrder("+1222234567", "+1522234533", "+1234567890");
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
}
