package com.fiap.fintech.dao;

import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.factory.ConnectionFactory;
import com.fiap.fintech.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao implements AutoCloseable {

    private final Connection conexao;

    public TransactionDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Transaction transaction) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement(
                "insert into transactions (id, user_id, amount, type, description, transaction_date, bank_account_id, yield, created_at, updated_at) " +
                        "values (seq_transactions.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        popularStatement(transaction, stmt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void atualizar(Transaction transaction) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement(
                "update transactions set user_id = ?, amount = ?, type = ?, description = ?, transaction_date = ?, " +
                        "bank_account_id = ?, yield = ?, created_at = ?, updated_at = ? where id = ?");
        popularStatement(transaction, stmt);
        stmt.setInt(10, transaction.getId());
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    public Transaction pesquisar(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from transactions where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Integer yield = rs.getObject("yield") == null ? null : rs.getInt("yield");
            Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("amount"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getDate("transaction_date"),
                    rs.getInt("bank_account_id"),
                    yield,
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"));
            rs.close();
            stmt.close();
            return transaction;
        }

        rs.close();
        stmt.close();
        throw new EntidadeNaoEncontrada();
    }

    public List<Transaction> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from transactions order by transaction_date desc, id");
        ResultSet rs = stmt.executeQuery();
        List<Transaction> lista = new ArrayList<>();

        while (rs.next()) {
            Integer yield = rs.getObject("yield") == null ? null : rs.getInt("yield");
            lista.add(new Transaction(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("amount"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getDate("transaction_date"),
                    rs.getInt("bank_account_id"),
                    yield,
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")));
        }

        rs.close();
        stmt.close();
        return lista;
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete from transactions where id = ?");
        stmt.setInt(1, id);
        int linhas = stmt.executeUpdate();
        stmt.close();
        if (linhas == 0) {
            throw new EntidadeNaoEncontrada();
        }
    }

    private static void popularStatement(Transaction transaction, PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, transaction.getUserId());
        stmt.setDouble(2, transaction.getAmount());
        stmt.setString(3, transaction.getType());
        stmt.setString(4, transaction.getDescription());
        stmt.setDate(5, DaoUtils.toSqlDate(transaction.getTransactionDate()));
        stmt.setInt(6, transaction.getBankAccountId());
        DaoUtils.setNullableInteger(stmt, 7, transaction.getYield());
        stmt.setDate(8, DaoUtils.toSqlDate(transaction.getCreatedAt()));
        stmt.setDate(9, DaoUtils.toSqlDate(transaction.getUpdatedAt()));
    }

    @Override
    public void close() throws SQLException {
        conexao.close();
    }
}
