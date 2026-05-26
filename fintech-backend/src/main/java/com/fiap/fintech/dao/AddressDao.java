package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDao implements AutoCloseable {

    private final Connection conexao;

    public AddressDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Address address) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into address (id, user_id, address_string, zip_code, created_at, updated_at) " +
                        "values (seq_address.nextval, ?, ?, ?, ?, ?)");
        popularStatement(address, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void atualizar(Address address) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update address set user_id = ?, address_string = ?, zip_code = ?, created_at = ?, updated_at = ? " +
                        "where id = ?");
        popularStatement(address, stmt);
        stmt.setInt(6, address.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public Address pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from address where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Address address = new Address(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("address_string"),
                    rs.getString("zip_code"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return address;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<Address> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from address order by id");
        ResultSet rs = stmt.executeQuery();
        List<Address> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new Address(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("address_string"),
                    rs.getString("zip_code"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from address where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(Address address, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, address.getUserId());
        stmt.setString(2, address.getAddressString());
        stmt.setString(3, address.getZipCode());
        stmt.setDate(4, DaoUtils.toSqlDate(address.getCreatedAt()));
        stmt.setDate(5, DaoUtils.toSqlDate(address.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
