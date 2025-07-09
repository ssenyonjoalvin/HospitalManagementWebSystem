package org.pahappa.systems.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.util.HibernateUtil;

@ApplicationScoped
public class PatientDAO {
    public Patient findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Patient.class, id);
        }
    }
}