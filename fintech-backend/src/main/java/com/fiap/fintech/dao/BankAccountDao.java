package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.BankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDao implements AutoCloseable {

    private final Connection conexao;

    public BankAccountDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(BankAccount bankAccount) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into bank_account (id, user_id, bank, type, description, agency, account_number, created_at, updated_at) " +
                        "values (seq_bank_account.nextval, ?, ?, ?, ?, ?, ?, ?, ?)");
        popularStatement(bankAccount, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void atualizar(BankAccount bankAccount) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update bank_account set user_id = ?, bank = ?, type = ?, description = ?, agency = ?, " +
                        "account_number = ?, created_at = ?, updated_at = ? where id = ?");
        popularStatement(bankAccount, stmt);
        stmt.setInt(9, bankAccount.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public BankAccount pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from bank_account where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            BankAccount bankAccount = new BankAccount(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("bank"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("agency"),
                    rs.getString("account_number"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return bankAccount;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<BankAccount> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from bank_account order by id");
        ResultSet rs = stmt.executeQuery();
        List<BankAccount> lista = new ArrayList<>();

        while (rs.next()) {
            lista.add(new BankAccount(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("bank"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("agency"),
                    rs.getString("account_number"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from bank_account where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(BankAccount bankAccount, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, bankAccount.getUserId());
        stmt.setString(2, bankAccount.getBank());
        stmt.setString(3, bankAccount.getType());
        stmt.setString(4, bankAccount.getDescription());
        stmt.setString(5, bankAccount.getAgency());
        stmt.setString(6, bankAccount.getAccountNumber());
        stmt.setDate(7, DaoUtils.toSqlDate(bankAccount.getCreatedAt()));
        stmt.setDate(8, DaoUtils.toSqlDate(bankAccount.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
