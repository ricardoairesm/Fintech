package com.fiap.fintech.model;

import java.util.Date;

public class CompletedChallenge {
    private int id;
    private int userId;
    private int challengeId;
    private Date completedAt;
    private Date createdAt;
    private Date updatedAt;

    public CompletedChallenge(int id, int userId, int challengeId, Date completedAt, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.challengeId = challengeId;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getChallengeId() { return challengeId; }
    public Date getCompletedAt() { return completedAt; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void registerCompletion() {
        System.out.println("Registraria a conclusao do desafio pelo usuario.");
    }
}
