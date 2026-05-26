package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.Challenge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChallengeDao implements AutoCloseable {

    private final Connection conexao;

    public ChallengeDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Challenge challenge) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into challenge (id, title, min_tier_id, start_date, end_date, active, reward_id, progress, created_at, updated_at) " +
                        "values (seq_challenge.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        popularStatement(challenge, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void insert(Challenge challenge) throws SQLException {
        cadastrar(challenge);
    }

    public void atualizar(Challenge challenge) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update challenge set title = ?, min_tier_id = ?, start_date = ?, end_date = ?, active = ?, reward_id = ?, " +
                        "progress = ?, created_at = ?, updated_at = ? where id = ?");
        popularStatement(challenge, stmt);
        stmt.setInt(10, challenge.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public Challenge pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from challenge where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Challenge challenge = new Challenge(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("min_tier_id"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    DaoUtils.getBooleanFromNumber(rs.getInt("active")),
                    rs.getInt("reward_id"),
                    rs.getInt("progress"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return challenge;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<Challenge> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from challenge order by id");
        ResultSet rs = stmt.executeQuery();
        List<Challenge> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new Challenge(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("min_tier_id"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    DaoUtils.getBooleanFromNumber(rs.getInt("active")),
                    rs.getInt("reward_id"),
                    rs.getInt("progress"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public List<Challenge> getAll() throws SQLException {
        return listar();
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from challenge where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(Challenge challenge, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, challenge.getTitle());
        stmt.setInt(2, challenge.getMinTierId());
        stmt.setDate(3, DaoUtils.toSqlDate(challenge.getStartDate()));
        stmt.setDate(4, DaoUtils.toSqlDate(challenge.getEndDate()));
        stmt.setInt(5, challenge.isActive() ? 1 : 0);
        stmt.setInt(6, challenge.getRewardId());
        stmt.setInt(7, challenge.getProgress());
        stmt.setDate(8, DaoUtils.toSqlDate(challenge.getCreatedAt()));
        stmt.setDate(9, DaoUtils.toSqlDate(challenge.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
