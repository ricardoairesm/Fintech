package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.CompletedChallenge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompletedChallengeDao implements AutoCloseable {

    private final Connection conexao;

    public CompletedChallengeDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(CompletedChallenge completedChallenge) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into completed_challenge (id, user_id, challenge_id, completed_at, created_at, updated_at) " +
                        "values (seq_completed_challenge.nextval, ?, ?, ?, ?, ?)");
        popularStatement(completedChallenge, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void atualizar(CompletedChallenge completedChallenge) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update completed_challenge set user_id = ?, challenge_id = ?, completed_at = ?, " +
                        "created_at = ?, updated_at = ? where id = ?");
        popularStatement(completedChallenge, stmt);
        stmt.setInt(6, completedChallenge.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public CompletedChallenge pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from completed_challenge where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CompletedChallenge completedChallenge = new CompletedChallenge(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("challenge_id"),
                    rs.getDate("completed_at"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return completedChallenge;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<CompletedChallenge> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from completed_challenge order by id");
        ResultSet rs = stmt.executeQuery();
        List<CompletedChallenge> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new CompletedChallenge(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("challenge_id"),
                    rs.getDate("completed_at"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from completed_challenge where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(CompletedChallenge completedChallenge, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, completedChallenge.getUserId());
        stmt.setInt(2, completedChallenge.getChallengeId());
        stmt.setDate(3, DaoUtils.toSqlDate(completedChallenge.getCompletedAt()));
        stmt.setDate(4, DaoUtils.toSqlDate(completedChallenge.getCreatedAt()));
        stmt.setDate(5, DaoUtils.toSqlDate(completedChallenge.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
