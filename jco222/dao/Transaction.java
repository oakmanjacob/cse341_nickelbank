package dao;

import oracle.jdbc.OraclePreparedStatement;
import util.DBManager;

import java.sql.*;

public class Transaction {

    protected long transaction_id;
    protected long parent_transaction_id;
    protected Person person;
    protected Branch branch;
    protected double amount;
    protected String type;
    protected String status;

    public Transaction(double amount, Person person, Branch branch, String type, String status)
    {
        this.amount = amount;
        this.person = person;
        this.branch = branch;
        this.type = type;
        this.status = status;
    }

    public boolean save()
    {
        if (this.transaction_id == 0)
        {
            Connection conn = DBManager.getConnection();

            String query = "insert into transaction" +
                    "(person_id, branch_id, amount, type, status)" +
                    "values (?, ?, ?, ?, ?) returning transaction_id into ?";
            try (
                    OraclePreparedStatement ps = (OraclePreparedStatement)conn.prepareStatement(query);
            ) {
                conn.setAutoCommit(false);

                if (this.person != null) {
                    ps.setLong(1, this.person.getPersonId());
                }
                else
                {
                    ps.setNull(1, Types.NUMERIC);
                }

                if (this.branch != null) {
                    ps.setLong(2, this.branch.getBranchId());
                }
                else
                {
                    ps.setNull(2, Types.NUMERIC);
                }

                ps.setDouble(3, this.amount);
                ps.setString(4, this.type);
                ps.setString(5, this.status);
                ps.registerReturnParameter(6, Types.NUMERIC);

                if (ps.executeUpdate() == 0) {
                    return false;
                }

                ResultSet rs = ps.getReturnResultSet();

                if (rs != null && rs.next()) {
                    this.transaction_id = rs.getLong(1);
                }
                else
                {
                    throw new SQLException("Row possibly not inserted");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean delete()
    {
        return false;
    }

    public long getTransactionId() {
        return transaction_id;
    }

    public long getParentTransactionId() {
        return parent_transaction_id;
    }

    public void setParentTransactionId(long parent_transaction_id) {
        this.parent_transaction_id = parent_transaction_id;
    }

    public Person getPerson() {
        return person;
    }

    public Branch getBranch() {
        return branch;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
