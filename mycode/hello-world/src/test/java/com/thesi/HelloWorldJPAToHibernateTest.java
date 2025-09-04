package com.thesi;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldJPAToHibernateTest {
    /**
     * get Hibernate Session factory from jpa EntityManagerFactory
     * </ br>
     * Since version 2.0, JPA provides easy access to the APIs of the underlying implementations.
     * The EntityManager and the EntityManagerFactory provide an unwrap method which returns
     * the corresponding classes of the JPA implementation. In the case of Hibernate,
     * these are the Session and the SessionFactory.
     * @param entityManagerFactory jpa EntityManagerFactory
     * @return Hibernate Session
     */
    private static SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Test
    public void storeLoadMessage() {

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch02");
             SessionFactory sessionFactory = getSessionFactory(emf);
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            Message message = new Message();
            message.setText("Hello World from JPA to Hibernate!");

            session.persist(message);

            session.getTransaction().commit();
            // INSERT into MESSAGE (ID, TEXT)
            // values (1, 'Hello World from JPA to Hibernate!')
            session.beginTransaction();

            CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);
            criteriaQuery.from(Message.class);

            List<Message> messages = session.createQuery(criteriaQuery).getResultList();
            // SELECT * from MESSAGE

            session.getTransaction().commit();

            assertAll(
                    () -> assertEquals(1, messages.size()),
                    () -> assertEquals("Hello World from JPA to Hibernate!", messages.get(0).getText())
            );

        }
    }
}
