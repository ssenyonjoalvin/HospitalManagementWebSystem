package org.pahappa.systems.services.patient.impl;

import org.pahappa.systems.models.Patient;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.models.PatientTypeEntity;
import org.pahappa.systems.repository.UserDAO;
import org.pahappa.systems.services.patient.PatientService;
import org.pahappa.systems.services.RoleService;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.enterprise.context.ApplicationScoped;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class PatientServiceImpl implements PatientService {

    // The service is the only layer that should interact with the DAO
    private final UserDAO userDAO = new UserDAO();

    @Inject
    private RoleService roleService;

    @Override
    public List<Patient> getAllPatients() {
        return userDAO.getAllRecords().stream()
                .filter(user -> user.getRole() != null && "PATIENT".equals(user.getRole().getName()) && user instanceof Patient)
                .map(user -> (Patient) user)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> getPatientsByType(PatientTypeEntity patientType) {
        return getAllPatients().stream()
                .filter(patient -> patient.getPatientType() != null && patient.getPatientType().equals(patientType))
                .collect(Collectors.toList());
    }

    @Override
    public void savePatient(Patient patient) {
        if (patient.getRole() == null) {
            patient.setRole(roleService.getAll().stream().filter(r -> "PATIENT".equals(r.getName())).findFirst().orElse(null));
        }
        userDAO.saveRecord(patient);
    }

//    private static String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hash = md.digest(password.getBytes());
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hash) {
//                String hex = Integer.toHexString(0xff & b);
//                if (hex.length() == 1)
//                    hexString.append('0');
//                hexString.append(hex);
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error hashing password", e);
//        }
//    }

    public Map<String, List<Patient>> getPatientLists() {
        List<Patient> all = getAllPatients();
        List<Patient> hospitalized = all.stream()
                .filter(p -> p.getPatientType() != null && "INPATIENT".equals(p.getPatientType().getName()))
                .collect(Collectors.toList());
        List<Patient> outpatients = all.stream()
                .filter(p -> p.getPatientType() != null && "OUTPATIENT".equals(p.getPatientType().getName()))
                .collect(Collectors.toList());
        Map<String, List<Patient>> result = new HashMap<>();
        result.put("all", all);
        result.put("hospitalized", hospitalized);
        result.put("outpatients", outpatients);
        return result;
    }

    @Override
    public void updatePatient(Patient patient) {
        // Update the patient record in the DAO
        userDAO.updateRecord(patient);
    }

    @Override
    public void deletePatient(Patient patient) {
        // Soft-delete: you could set a status, or for now, remove from DAO
        userDAO.deleteRecord(patient.getId());
    }

    @Override
    public int countAll() {
        return getAllPatients().size();
    }
}