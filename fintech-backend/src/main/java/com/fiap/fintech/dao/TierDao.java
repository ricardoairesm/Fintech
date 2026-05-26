package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.Tier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TierDao implements AutoCloseable {

    private final Connection conexao;

    public TierDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Tier tier) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into tier (id, name, min_points_required, hierarchy, created_at, updated_at) " +
                        "values (seq_tier.nextval, ?, ?, ?, ?, ?)");
        popularStatement(tier, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void insert(Tier tier) throws SQLException {
        cadastrar(tier);
    }

    public void atualizar(Tier tier) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update tier set name = ?, min_points_required = ?, hierarchy = ?, created_at = ?, updated_at = ? where id = ?");
        popularStatement(tier, stmt);
        stmt.setInt(6, tier.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public Tier pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from tier where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Tier tier = new Tier(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("min_points_required"),
                    rs.getInt("hierarchy"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return tier;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<Tier> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from tier order by hierarchy");
        ResultSet rs = stmt.executeQuery();
        List<Tier> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new Tier(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("min_points_required"),
                    rs.getInt("hierarchy"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public List<Tier> getAll() throws SQLException {
        return listar();
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from tier where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(Tier tier, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, tier.getName());
        stmt.setInt(2, tier.getMinPointsRequired());
        stmt.setInt(3, tier.getHierarchy());
        stmt.setDate(4, DaoUtils.toSqlDate(tier.getCreatedAt()));
        stmt.setDate(5, DaoUtils.toSqlDate(tier.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
