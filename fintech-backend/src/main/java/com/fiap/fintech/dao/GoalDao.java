package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.Goal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoalDao implements AutoCloseable {

    private final Connection conexao;

    public GoalDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Goal goal) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into goal (id, user_id, title, amount, saved_amount, limit_date, created_at, updated_at) " +
                        "values (seq_goal.nextval, ?, ?, ?, ?, ?, ?, ?)");
        popularStatement(goal, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void atualizar(Goal goal) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update goal set user_id = ?, title = ?, amount = ?, saved_amount = ?, limit_date = ?, " +
                        "created_at = ?, updated_at = ? where id = ?");
        popularStatement(goal, stmt);
        stmt.setInt(8, goal.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public Goal pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from goal where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Goal goal = new Goal(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getDouble("amount"),
                    rs.getDouble("saved_amount"),
                    rs.getDate("limit_date"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return goal;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<Goal> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from goal order by id");
        ResultSet rs = stmt.executeQuery();
        List<Goal> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new Goal(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getDouble("amount"),
                    rs.getDouble("saved_amount"),
                    rs.getDate("limit_date"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from goal where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(Goal goal, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, goal.getUserId());
        stmt.setString(2, goal.getTitle());
        stmt.setDouble(3, goal.getAmount());
        stmt.setDouble(4, goal.getSavedAmount());
        stmt.setDate(5, DaoUtils.toSqlDate(goal.getLimitDate()));
        stmt.setDate(6, DaoUtils.toSqlDate(goal.getCreatedAt()));
        stmt.setDate(7, DaoUtils.toSqlDate(goal.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
