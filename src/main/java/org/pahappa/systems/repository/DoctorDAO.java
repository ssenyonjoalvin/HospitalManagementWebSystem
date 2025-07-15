package org.pahappa.systems.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.util.HibernateUtil;
import org.pahappa.systems.models.UserAccount;

@ApplicationScoped
public class DoctorDAO {
    public Doctor findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Doctor.class, id);
        }
    }

    public Doctor getByUserAccount(UserAccount userAccount) {
        try (org.hibernate.Session session = org.pahappa.systems.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Doctor WHERE userAccount = :userAccount", Doctor.class)
                    .setParameter("userAccount", userAccount)
                    .uniqueResult();
        }
    }
}