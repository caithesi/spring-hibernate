package com.thesi;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldJPAContainerTest {
    /*
    pay attention to how data was written in persistence.xml, make sure they are map with data config for below container
     */
    private static final MySQLContainer<?> MY_SQL_CONTAINER =
            new MySQLContainer<>("mysql:8.0.43-debian")
                    .withDatabaseName("CH02")
                    .withUsername("root")
                    .withPassword("");
    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    static void init() {
        MY_SQL_CONTAINER.start();

//        Map<String, String> props = new HashMap<>();
//        props.put("jakarta.persistence.jdbc.url", MY_SQL_CONTAINER.getJdbcUrl());
//        props.put("jakarta.persistence.jdbc.user", MY_SQL_CONTAINER.getUsername());
//        props.put("jakarta.persistence.jdbc.password", MY_SQL_CONTAINER.getPassword());

//        emf = Persistence.createEntityManagerFactory("ch02", props);
        emf = Persistence.createEntityManagerFactory("ch02");
    }

    @AfterAll
    static void tearDown() {
        if (emf != null) {
            emf.close();
        }
        MY_SQL_CONTAINER.stop();
    }

    @BeforeEach
    void setUpEntityManager() {
        em = emf.createEntityManager();
    }

    @AfterEach
    void closeEntityManager() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    public void storeLoadMessage() {


        em.getTransaction().begin();

        Message message = new Message();
        message.setText("Hello World!");

        em.persist(message);

        em.getTransaction().commit();
        //INSERT into MESSAGE (ID, TEXT) values (1, 'Hello World!')

        em.getTransaction().begin();

        List<Message> messages =
                em.createQuery("select m from Message m", Message.class).getResultList();
        //SELECT * from MESSAGE

        messages.get(messages.size() - 1).setText("Hello World from JPA!");

        em.getTransaction().commit();
        //UPDATE MESSAGE set TEXT = 'Hello World from JPA!' where ID = 1

        assertAll(
                () -> assertEquals(1, messages.size()),
                () -> assertEquals("Hello World from JPA!", messages.get(0).getText())
        );
    }
}
