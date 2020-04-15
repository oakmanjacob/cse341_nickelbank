package dao;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.proxy.annotation.Pre;
import util.DBManager;

import java.sql.*;
import java.util.ArrayList;

public class Account {
    private long account_id;
    private long account_number;
    private String type;
    private double interest_rate;
    private double min_balance;
    private Timestamp created;

    private ArrayList<Person> owners;

    private boolean connected;

    public Account() {
        this.owners = new ArrayList<Person>();
    }

    public Account(String type) {
        this.type = type;
        this.owners = new ArrayList<Person>();
    }

    /**
     * Call PL/SQL function to calculate the current balance of the account
     *
     * @return current account balance
     */
    public double getBalance() {
        // TODO implement method
        return -1;
    }

    public boolean save() {
        Connection conn = DBManager.getConnection();

        if (!connected) {
            String query = "insert into account" +
                    "(type, interest_rate, min_balance)" +
                    "values (?, ?, ?) returning account_id, account_number into ?, ?";
            try (
                    OraclePreparedStatement ps = (OraclePreparedStatement)conn.prepareStatement(query);
            ) {
                conn.setAutoCommit(false);
                ps.setString(1, this.type);
                ps.setDouble(2, this.interest_rate);
                ps.setDouble(3, this.min_balance);
                ps.registerReturnParameter(4, Types.NUMERIC);
                ps.registerReturnParameter(5, Types.NUMERIC);

                if (ps.executeUpdate() == 0) {
                    return false;
                }

                ResultSet rs = ps.getReturnResultSet();

                if (rs != null && rs.next()) {
                    this.account_id = rs.getLong(1);
                    this.account_number = rs.getLong(2);
                }
                else
                {
                    throw new SQLException("Row possibly not inserted or something");
                }

                for (Person person : this.owners) {
                    PreparedStatement join = conn.prepareStatement("insert into person_account " +
                            "(person_id, account_id) values (?, ?)");

                    join.setLong(1, person.getPersonId());
                    join.setLong(2, this.account_id);

                    join.executeUpdate();
                }

                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            this.connected = true;
            return true;
        }
        return false;
    }

    public long getAccountId() {
        return account_id;
    }

    public void setAccountId(long account_id) {
        this.account_id = account_id;
    }

    public long getAccountNumber() {
        return account_number;
    }

    public void setAccountNumber(long account_number) {
        this.account_number = account_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getInterestRate() {
        return interest_rate;
    }

    public void setInterestRate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public double getMinBalance() {
        return min_balance;
    }

    public void setMinBalance(double min_balance) {
        this.min_balance = min_balance;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void addPerson(Person person)
    {
        this.owners.add(person);
    }
}
