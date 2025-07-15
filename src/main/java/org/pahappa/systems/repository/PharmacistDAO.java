package org.pahappa.systems.repository;

import org.pahappa.systems.models.Pharmacist;
import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PharmacistDAO {
    public Pharmacist getByUserAccount(UserAccount userAccount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pharmacist WHERE userAccount = :userAccount", Pharmacist.class)
                    .setParameter("userAccount", userAccount)
                    .uniqueResult();
        }
    }
}