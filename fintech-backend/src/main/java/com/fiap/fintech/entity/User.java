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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users")
    @SequenceGenerator(name = "seq_users", sequenceName = "seq_users", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "celphone")
    private String celphone;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "tier_id")
    private Integer tierId;

    @Column(name = "points")
    private Integer points;

    @Column(name = "main_address_id")
    private Integer mainAddressId;

    @Column(name = "monthly_income")
    private Integer monthlyIncome;

    @Column(name = "monthly_spending")
    private Integer monthlySpending;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCelphone() { return celphone; }
    public void setCelphone(String celphone) { this.celphone = celphone; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public Integer getTierId() { return tierId; }
    public void setTierId(Integer tierId) { this.tierId = tierId; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public Integer getMainAddressId() { return mainAddressId; }
    public void setMainAddressId(Integer mainAddressId) { this.mainAddressId = mainAddressId; }
    public Integer getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Integer monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public Integer getMonthlySpending() { return monthlySpending; }
    public void setMonthlySpending(Integer monthlySpending) { this.monthlySpending = monthlySpending; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
