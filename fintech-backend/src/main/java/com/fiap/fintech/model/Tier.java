package com.fiap.fintech.model;

import java.util.Date;

public class Tier {
    private int id;
    private String name;
    private int minPointsRequired;
    private int hierarchy;
    private Date createdAt;
    private Date updatedAt;

    public Tier(int id, String name, int minPointsRequired, int hierarchy, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.minPointsRequired = minPointsRequired;
        this.hierarchy = hierarchy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getMinPointsRequired() { return minPointsRequired; }
    public int getHierarchy() { return hierarchy; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria o tier no banco de dados.");
    }

    public boolean matchesPoints(int points) {
        return points >= minPointsRequired;
    }
}
