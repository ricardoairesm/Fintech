package com.fiap.fintech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "tier")
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tier")
    @SequenceGenerator(name = "seq_tier", sequenceName = "seq_tier", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_points_required")
    private Integer minPointsRequired;

    @Column(name = "hierarchy")
    private Integer hierarchy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getMinPointsRequired() { return minPointsRequired; }
    public void setMinPointsRequired(Integer minPointsRequired) { this.minPointsRequired = minPointsRequired; }
    public Integer getHierarchy() { return hierarchy; }
    public void setHierarchy(Integer hierarchy) { this.hierarchy = hierarchy; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
