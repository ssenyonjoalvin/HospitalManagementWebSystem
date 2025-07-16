package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Medicine;
import org.pahappa.systems.repository.MedicineDAO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named("medicineBean")
@ViewScoped
public class MedicineBean implements Serializable {
    private Medicine newMedicine = new Medicine();
    private List<Medicine> allMedicines;
    private Medicine selectedMedicine;

    @Inject
    private MedicineDAO medicineDAO;

    @PostConstruct
    public void init() {
        allMedicines = medicineDAO.getAllRecords();
    }

    public void saveMedicine() {
        try {
            medicineDAO.saveRecord(newMedicine);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicine added successfully!", null));
            newMedicine = new Medicine(); // Reset form
            allMedicines = medicineDAO.getAllRecords(); // Refresh list after add
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding medicine: " + e.getMessage(), null));
        }
    }

    public void prepareEdit(Medicine medicine) {
        this.selectedMedicine = new Medicine();
        this.selectedMedicine.setId(medicine.getId());
        this.selectedMedicine.setName(medicine.getName());
        this.selectedMedicine.setDescription(medicine.getDescription());
        this.selectedMedicine.setPrice(medicine.getPrice());
        this.selectedMedicine.setActive(medicine.isActive());
    }

    public void updateMedicine() {
        try {
            medicineDAO.updateRecord(selectedMedicine);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicine updated successfully!", null));
            allMedicines = medicineDAO.getAllRecords(); // Refresh list
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating medicine: " + e.getMessage(), null));
        }
    }

    public void confirmDelete(Medicine medicine) {
        this.selectedMedicine = medicine;
    }

    public void deleteMedicine() {
        if (selectedMedicine != null) {
            medicineDAO.deleteRecord(selectedMedicine.getId());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Medicine deleted successfully!", null));
            allMedicines = medicineDAO.getAllRecords(); // Refresh list
            selectedMedicine = null;
        }
    }

    public Medicine getNewMedicine() {
        return newMedicine;
    }

    public void setNewMedicine(Medicine newMedicine) {
        this.newMedicine = newMedicine;
    }

    public List<Medicine> getAllMedicines() {
        return allMedicines;
    }

    public Medicine getSelectedMedicine() {
        return selectedMedicine;
    }

    public void setSelectedMedicine(Medicine selectedMedicine) {
        this.selectedMedicine = selectedMedicine;
    }
} 