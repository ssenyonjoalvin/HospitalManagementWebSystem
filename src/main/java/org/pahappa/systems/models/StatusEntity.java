package org.pahappa.systems.models;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public StatusEntity() {}
    public StatusEntity(String name) { this.name = name; }

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
        StatusEntity s = (StatusEntity) o;
        return id != null && id.equals(s.getId());
    }
    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }
} 