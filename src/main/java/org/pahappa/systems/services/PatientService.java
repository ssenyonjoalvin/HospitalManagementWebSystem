package org.pahappa.systems.services;

import org.pahappa.systems.enums.PatientType;
import org.pahappa.systems.models.Patient;

import java.util.List;

public interface PatientService {
        /**
         * Retrieves all users who are patients.
         * @return A list of Patient objects.
         */
        List<Patient> getAllPatients();

        /**
         * Retrieves patients filtered by their type (e.g., HOSPITALIZED, OUTPATIENT).
         * @param patientType The type of patient to filter by.
         * @return A list of matching Patient objects.
         */
        List<Patient> getPatientsByType(PatientType patientType);

        // You can add more methods here later, like:
        // Patient getPatientById(long id);
        // void savePatient(Patient patient);
    }

