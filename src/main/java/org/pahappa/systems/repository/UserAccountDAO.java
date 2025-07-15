package org.pahappa.systems.repository;

import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserAccountDAO {
    public UserAccount getByUserName(String userName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserAccount WHERE userName = :userName  ", UserAccount.class)
                    .setParameter("userName", userName)
                    .uniqueResult();
        }
    }
}