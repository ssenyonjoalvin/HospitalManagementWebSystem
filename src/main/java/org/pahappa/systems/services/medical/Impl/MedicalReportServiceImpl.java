package org.pahappa.systems.services.medical.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.MedicalReport;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.repository.MedicalReportDAO;
import java.util.List;

@ApplicationScoped
public class MedicalReportServiceImpl {
    @Inject
    private MedicalReportDAO medicalReportDAO;

    public List<MedicalReport> getReportsByPatient(Patient patient) {
        return medicalReportDAO.findByPatient(patient);
    }
}