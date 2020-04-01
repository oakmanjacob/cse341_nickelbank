package dao;

import util.DBManager;

import java.sql.*;

public class Account {
    private static final String table_name = "account";
    private long account_id;
    private long account_number;
    private String type;
    private double interest_rate;
    private double min_balance;
    private Timestamp created;

    private boolean saveTimestamp = false;

    public Account(String type) {
        this.account_id = 0;
        this.type = type;
    }

    public Account(long account_id) {
        Connection conn = DBManager.getConnection();
        String sql = "select * from account where account_id = ?;";

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setLong(1, account_id);
            ResultSet rs = ps.executeQuery();

            this.account_id = rs.getLong("account_id");
            this.account_number = rs.getLong("account_number");
            this.type = rs.getString("type");
            this.interest_rate = rs.getDouble("interest_rate");
            this.min_balance = rs.getDouble("min_balance");
            this.created = rs.getTimestamp("created");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setCreated(Timestamp created)
    {
        this.created = created;
        this.saveTimestamp = true;
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

        if (account_id == 0) {

            String field = this.type.equals("savings") ? "interest_rate" : "min_balance";
            String sql;

            if (this.saveTimestamp)
            {
                sql = "insert into account (type, " + field + ", created)" +
                        "values (?, ?, ?)";
            }
            else {
                sql = "insert into account (type, " + field + ")" +
                        "values (?, ?)";
            }

            try (
                    PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                ps.setString(1, this.type);

                if (this.type.equals("savings")) {
                    ps.setDouble(2, this.interest_rate);
                }
                else {
                    ps.setDouble(2, this.min_balance);
                }

                if (this.saveTimestamp)
                {
                    ps.setTimestamp(3, created);
                }

                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            this.saveTimestamp = false;
            return true;
        }
        return false;
    }
}
