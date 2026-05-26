package com.fiap.fintech;

import com.fiap.fintech.dao.ChallengeDao;
import com.fiap.fintech.dao.RewardDao;
import com.fiap.fintech.dao.TierDao;
import com.fiap.fintech.model.Challenge;
import com.fiap.fintech.model.Reward;
import com.fiap.fintech.model.Tier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Teste {

    private static final int TOTAL_REGISTROS = 5;
    private static final Random RANDOM = new Random();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        try {
            executarTesteDao();
        } catch (Exception e) {
            System.err.println("Falha ao executar os testes DAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executarTesteDao() throws Exception {
        String lote = String.valueOf(System.currentTimeMillis());

        TierDao tierDao = new TierDao();
        RewardDao rewardDao = new RewardDao();
        ChallengeDao challengeDao = new ChallengeDao();

        System.out.println("===== TESTE DAO FINTECH =====");
        System.out.println("Lote atual: " + lote);

        List<Tier> tiersNovos = criarTiers(lote);
        inserirTiers(tierDao, tiersNovos);
        List<Tier> tiersBanco = tierDao.getAll();
        listarTiers(tiersBanco);

        List<Reward> rewardsNovos = criarRewards(lote);
        inserirRewards(rewardDao, rewardsNovos);
        List<Reward> rewardsBanco = rewardDao.getAll();
        listarRewards(rewardsBanco);

        List<Tier> tiersDoLote = filtrarTiersDoLote(tiersBanco, lote);
        List<Reward> rewardsDoLote = filtrarRewardsDoLote(rewardsBanco, lote);
        List<Challenge> challengesNovos = criarChallenges(tiersDoLote, rewardsDoLote);
        inserirChallenges(challengeDao, challengesNovos);
        List<Challenge> challengesBanco = challengeDao.getAll();
        listarChallenges(challengesBanco);
    }

    private static List<Tier> criarTiers(String lote) {
        List<Tier> tiers = new ArrayList<>();
        for (int i = 0; i < TOTAL_REGISTROS; i++) {
            Date now = new Date();
            tiers.add(new Tier(
                    0,
                    "Tier_" + lote + "_" + i,
                    50 + RANDOM.nextInt(500),
                    1000 + i,
                    now,
                    now));
        }
        return tiers;
    }

    private static void inserirTiers(TierDao tierDao, List<Tier> tiers) throws Exception {
        System.out.println();
        System.out.println("===== INSERT TIER =====");
        for (Tier tier : tiers) {
            System.out.println("Inserindo tier -> nome: " + tier.getName()
                    + ", minPoints: " + tier.getMinPointsRequired()
                    + ", hierarchy: " + tier.getHierarchy());
            tierDao.insert(tier);
        }
    }

    private static void listarTiers(List<Tier> tiers) {
        System.out.println();
        System.out.println("===== GET ALL TIER =====");
        for (Tier tier : tiers) {
            System.out.println("id=" + tier.getId()
                    + ", nome=" + tier.getName()
                    + ", minPoints=" + tier.getMinPointsRequired()
                    + ", hierarchy=" + tier.getHierarchy());
        }
    }

    private static List<Reward> criarRewards(String lote) {
        List<Reward> rewards = new ArrayList<>();
        for (int i = 0; i < TOTAL_REGISTROS; i++) {
            Date now = new Date();
            rewards.add(new Reward(
                    0,
                    "Reward_" + lote + "_" + i,
                    "Descricao aleatoria " + (100 + RANDOM.nextInt(900)),
                    i % 2 == 0,
                    now,
                    now));
        }
        return rewards;
    }

    private static void inserirRewards(RewardDao rewardDao, List<Reward> rewards) throws Exception {
        System.out.println();
        System.out.println("===== INSERT REWARD =====");
        for (Reward reward : rewards) {
            System.out.println("Inserindo reward -> nome: " + reward.getName()
                    + ", descricao: " + reward.getDescription()
                    + ", active: " + reward.isActive());
            rewardDao.insert(reward);
        }
    }

    private static void listarRewards(List<Reward> rewards) {
        System.out.println();
        System.out.println("===== GET ALL REWARD =====");
        for (Reward reward : rewards) {
            System.out.println("id=" + reward.getId()
                    + ", nome=" + reward.getName()
                    + ", descricao=" + reward.getDescription()
                    + ", active=" + reward.isActive());
        }
    }

    private static List<Challenge> criarChallenges(List<Tier> tiers, List<Reward> rewards) {
        List<Challenge> challenges = new ArrayList<>();
        for (int i = 0; i < TOTAL_REGISTROS; i++) {
            Date createdAt = new Date();
            Date startDate = adicionarDias(createdAt, i);
            Date endDate = adicionarDias(createdAt, 10 + i);
            challenges.add(new Challenge(
                    0,
                    tiers.get(i).getId(),
                    startDate,
                    endDate,
                    i % 2 == 0,
                    rewards.get(i).getId(),
                    createdAt,
                    createdAt));
        }
        return challenges;
    }

    private static void inserirChallenges(ChallengeDao challengeDao, List<Challenge> challenges) throws Exception {
        System.out.println();
        System.out.println("===== INSERT CHALLENGE =====");
        for (Challenge challenge : challenges) {
            System.out.println("Inserindo challenge -> minTierId: " + challenge.getMinTierId()
                    + ", rewardId: " + challenge.getRewardId()
                    + ", startDate: " + DATE_FORMAT.format(challenge.getStartDate())
                    + ", endDate: " + DATE_FORMAT.format(challenge.getEndDate())
                    + ", active: " + challenge.isActive());
            challengeDao.insert(challenge);
        }
    }

    private static void listarChallenges(List<Challenge> challenges) {
        System.out.println();
        System.out.println("===== GET ALL CHALLENGE =====");
        for (Challenge challenge : challenges) {
            System.out.println("id=" + challenge.getId()
                    + ", minTierId=" + challenge.getMinTierId()
                    + ", rewardId=" + challenge.getRewardId()
                    + ", startDate=" + DATE_FORMAT.format(challenge.getStartDate())
                    + ", endDate=" + DATE_FORMAT.format(challenge.getEndDate())
                    + ", active=" + challenge.isActive());
        }
    }

    private static List<Tier> filtrarTiersDoLote(List<Tier> tiers, String lote) {
        List<Tier> filtrados = new ArrayList<>();
        for (Tier tier : tiers) {
            if (tier.getName().contains(lote)) {
                filtrados.add(tier);
            }
        }
        return filtrados;
    }

    private static List<Reward> filtrarRewardsDoLote(List<Reward> rewards, String lote) {
        List<Reward> filtrados = new ArrayList<>();
        for (Reward reward : rewards) {
            if (reward.getName().contains(lote)) {
                filtrados.add(reward);
            }
        }
        return filtrados;
    }

    private static Date adicionarDias(Date dataBase, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataBase);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }
}
