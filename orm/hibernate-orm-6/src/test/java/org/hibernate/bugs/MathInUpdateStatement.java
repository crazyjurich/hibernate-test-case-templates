package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class MathInUpdateStatement {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    public void testBase(String sql) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.createQuery(sql)
                .setParameter(1, BigDecimal.valueOf(1.23))
                .executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }
    @Test
    public void test1() {
        this.testBase("UPDATE Account1 a SET balance=balance + ?1");
    }

    @Test
    public void test2() {
        this.testBase("UPDATE Account2 a SET balance=balance + ?1");
    }

    @Test
    public void test3() {
        this.testBase("UPDATE Account3 a SET balance=balance + ?1");
    }

    @Entity(name = "Account1")
    @Table(name = "account1")
    public static class Account1 {
        @Id
        private Long id;

        private BigDecimal balance;
    }

    @Entity(name = "Account2")
    @Table(name = "account2")
    public static class Account2 {
        @Id
        private Long id;

        @Column(name="balance")
        private BigDecimal balance;
    }

    @Entity(name = "Account3")
    @Table(name = "account3")
    public static class Account3 {
        @Id
        private Long id;

        @Column(name="balance", precision = 5, scale = 2)
        private BigDecimal balance;
    }
}
