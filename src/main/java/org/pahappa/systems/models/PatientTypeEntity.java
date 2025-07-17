package org.pahappa.systems.models;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_types")
public class PatientTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public PatientTypeEntity() {}
    public PatientTypeEntity(String name) { this.name = name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientTypeEntity p = (PatientTypeEntity) o;
        return id != null && id.equals(p.getId());
    }
    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }
} 