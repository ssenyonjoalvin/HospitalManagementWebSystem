package org.pahappa.systems.repository;

import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.NonUniqueResultException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserAccountDAO {
    public UserAccount getByUserName(String userName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                return session.createQuery("FROM UserAccount WHERE userName = :userName  ", UserAccount.class)
                        .setParameter("userName", userName)
                        .uniqueResult();
            } catch (NonUniqueResultException e) {
                throw new RuntimeException("Multiple accounts found with the same username. Please contact the administrator.", e);
            }
        }
    }
}