package org.pahappa.systems.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.util.HibernateUtil;

@ApplicationScoped
public class DoctorDAO {
    public Doctor findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Doctor.class, id);
        }
    }
}