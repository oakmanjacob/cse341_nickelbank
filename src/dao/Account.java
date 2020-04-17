package dao;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.proxy.annotation.Pre;
import util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private long account_id;
    private long account_number;
    private String type;
    private double interest_rate;
    private double min_balance;
    private Timestamp created;

    private double balance;

    private ArrayList<Person> owners;

    private boolean connected;

    public Account() {
        this.owners = new ArrayList<Person>();
    }

    public Account(String type) {
        this.type = type;
        this.owners = new ArrayList<Person>();
    }

    public Account(long account_id, long account_number, String type, double balance, double interest_rate, double min_balance, Timestamp created)
    {
        this.connected = true;

        this.account_id = account_id;
        this.account_number = account_number;
        this.type = type;
        this.interest_rate = interest_rate;
        this.min_balance = min_balance;

        this.balance = balance;
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

    public static Account fromNumber(long account_number)
    {
        Connection conn = DBManager.getConnection();
        Account account = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT a.*, ab.balance " +
                            "FROM account a " +
                            "INNER JOIN account_balance ab " +
                            "ON a.account_id = ab.account_id " +
                            "WHERE a.account_number = ?");

            ps.setLong(1, account_number);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                account = new Account(
                        result.getLong("account_id"),
                        result.getLong("account_number"),
                        result.getString("type"),
                        result.getDouble("balance"),
                        result.getDouble("interest_rate"),
                        result.getDouble("min_balance"),
                        result.getTimestamp("created")
                );
            }
            else {
                return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return account;
    }

    public static Account fromDebitCard(long card_number)
    {
        Connection conn = DBManager.getConnection();
        Account account = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT a.*, ab.balance " +
                            "FROM card c " +
                            "INNER JOIN card_debit cd " +
                            "ON c.card_id = cd.card_id " +
                            "INNER JOIN account a " +
                            "ON cd.account_id = a.account_id " +
                            "INNER JOIN account_balance ab " +
                            "ON a.account_id = ab.account_id " +
                            "WHERE c.card_number = ?");

            ps.setLong(1, card_number);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                account = new Account(
                        result.getLong("account_id"),
                        result.getLong("account_number"),
                        result.getString("type"),
                        result.getDouble("balance"),
                        result.getDouble("interest_rate"),
                        result.getDouble("min_balance"),
                        result.getTimestamp("created"));
            }
            else {
                return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return account;
    }

    public static List<Account> getAllFromEmail(String email)
    {
        Connection conn = DBManager.getConnection();
        List<Account> account_list = new ArrayList<Account>();

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT a.*, ab.balance " +
                            "FROM person p " +
                            "INNER JOIN person_account pa " +
                            "ON p.person_id = pa.person_id " +
                            "INNER JOIN account a " +
                            "ON pa.account_id = a.account_id " +
                            "INNER JOIN account_balance ab " +
                            "ON a.account_id = ab.account_id " +
                            "WHERE p.email = ? " +
                            "ORDER BY a.created ASC");

            ps.setString(1, email);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                do {
                    account_list.add(new Account(
                            result.getLong("account_id"),
                            result.getLong("account_number"),
                            result.getString("type"),
                            result.getDouble("balance"),
                            result.getDouble("interest_rate"),
                            result.getDouble("min_balance"),
                            result.getTimestamp("created")));
                }
                while (result.next());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return account_list;
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

    public long getLastFour()
    {
        return account_number % 10000;
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

    public double getBalance() {
        return this.balance;
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

    public String toString()
    {
        return this.type.toUpperCase() + " - *********" + this.getLastFour() + " balance: $" + this.balance;
    }
}
