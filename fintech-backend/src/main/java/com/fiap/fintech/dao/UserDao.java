package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements AutoCloseable {

    private final Connection conexao;

    public UserDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(User user) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into users (id, username, password, email, celphone, user_type, tier_id, points, main_address_id, monthly_income, monthly_spending, created_at, updated_at) " +
                        "values (seq_users.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        popularStatement(user, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void atualizar(User user) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update users set username = ?, password = ?, email = ?, celphone = ?, user_type = ?, tier_id = ?, points = ?, " +
                        "main_address_id = ?, monthly_income = ?, monthly_spending = ?, created_at = ?, updated_at = ? where id = ?");
        popularStatement(user, stmt);
        stmt.setInt(13, user.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public User pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from users where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Integer mainAddressId = rs.getObject("main_address_id") == null ? null : rs.getInt("main_address_id");
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("celphone"),
                    rs.getString("user_type"),
                    rs.getInt("tier_id"),
                    rs.getInt("points"),
                    mainAddressId,
                    rs.getInt("monthly_income"),
                    rs.getInt("monthly_spending"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return user;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<User> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from users order by id");
        ResultSet rs = stmt.executeQuery();
        List<User> lista = new ArrayList<>();

        while (rs.next()) {
            Integer mainAddressId = rs.getObject("main_address_id") == null ? null : rs.getInt("main_address_id");
            lista.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("celphone"),
                    rs.getString("user_type"),
                    rs.getInt("tier_id"),
                    rs.getInt("points"),
                    mainAddressId,
                    rs.getInt("monthly_income"),
                    rs.getInt("monthly_spending"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from users where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(User user, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getCelphone());
        stmt.setString(5, user.getUserType());
        stmt.setInt(6, user.getTierId());
        stmt.setInt(7, user.getPoints());
        DaoUtils.setNullableInteger(stmt, 8, user.getMainAddressId());
        stmt.setInt(9, user.getMonthlyIncome());
        stmt.setInt(10, user.getMonthlySpending());
        stmt.setDate(11, DaoUtils.toSqlDate(user.getCreatedAt()));
        stmt.setDate(12, DaoUtils.toSqlDate(user.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
