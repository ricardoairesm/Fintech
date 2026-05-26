package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.Reward;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RewardDao implements AutoCloseable {

    private final Connection conexao;

    public RewardDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Reward reward) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into reward (id, name, description, active, created_at, updated_at) " +
                        "values (seq_reward.nextval, ?, ?, ?, ?, ?)");
        popularStatement(reward, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void insert(Reward reward) throws SQLException {
        cadastrar(reward);
    }

    public void atualizar(Reward reward) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update reward set name = ?, description = ?, active = ?, created_at = ?, updated_at = ? where id = ?");
        popularStatement(reward, stmt);
        stmt.setInt(6, reward.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public Reward pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from reward where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Reward reward = new Reward(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    DaoUtils.getBooleanFromNumber(rs.getInt("active")),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return reward;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<Reward> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from reward order by id");
        ResultSet rs = stmt.executeQuery();
        List<Reward> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new Reward(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    DaoUtils.getBooleanFromNumber(rs.getInt("active")),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public List<Reward> getAll() throws SQLException {
        return listar();
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from reward where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(Reward reward, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, reward.getName());
        stmt.setString(2, reward.getDescription());
        stmt.setInt(3, reward.isActive() ? 1 : 0);
        stmt.setDate(4, DaoUtils.toSqlDate(reward.getCreatedAt()));
        stmt.setDate(5, DaoUtils.toSqlDate(reward.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
