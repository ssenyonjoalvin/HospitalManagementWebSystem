package org.pahappa.systems.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.pahappa.systems.models.MedicalReport;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.util.HibernateUtil;

import java.util.List;

@ApplicationScoped
public class MedicalReportDAO {
    public List<MedicalReport> findByPatient(Patient patient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM MedicalReport mr WHERE mr.patient = :patient ORDER BY mr.createdAt DESC",
                    MedicalReport.class)
                    .setParameter("patient", patient)
                    .list();
        }
    }
}