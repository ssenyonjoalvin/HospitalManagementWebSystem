package org.pahappa.systems.repository;

import org.pahappa.systems.models.Receptionist;
import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReceptionistDAO {
    public Receptionist getByUserAccount(UserAccount userAccount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Receptionist WHERE userAccount = :userAccount", Receptionist.class)
                    .setParameter("userAccount", userAccount)
                    .uniqueResult();
        }
    }
}