package com.fiap.fintech.model;

import java.util.Date;

public class Reward {
    private int id;
    private String name;
    private String description;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;

    public Reward(int id, String name, String description, boolean active, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria a recompensa no banco de dados.");
    }

    public void activate() {
        this.active = true;
        System.out.println("Ativaria a recompensa.");
    }

    public void deactivate() {
        this.active = false;
        System.out.println("Desativaria a recompensa.");
    }
}
