package com.fiap.fintech.model;

import java.util.Date;

public class Challenge {
    private int id;
    private String title;
    private int minTierId;
    private Date startDate;
    private Date endDate;
    private boolean active;
    private int rewardId;
    private int progress;
    private Date createdAt;
    private Date updatedAt;

    public Challenge(int id, int minTierId, Date startDate, Date endDate, boolean active,
            int rewardId, Date createdAt, Date updatedAt) {
        this(id, "Desafio financeiro", minTierId, startDate, endDate, active, rewardId, 0, createdAt, updatedAt);
    }

    public Challenge(int id, String title, int minTierId, Date startDate, Date endDate, boolean active,
            int rewardId, int progress, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.minTierId = minTierId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.rewardId = rewardId;
        this.progress = progress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getMinTierId() { return minTierId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public boolean isActive() { return active; }
    public int getRewardId() { return rewardId; }
    public int getProgress() { return progress; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria o desafio no banco de dados.");
    }

    public void activate() {
        this.active = true;
        System.out.println("Ativaria o desafio.");
    }

    public void deactivate() {
        this.active = false;
        System.out.println("Desativaria o desafio.");
    }
}
