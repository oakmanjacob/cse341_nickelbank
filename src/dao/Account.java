package dao;

import java.sql.*;
import java.util.Random;

import util.DBManager;

public class Account implements DBObject {
    private static final String table_name = "account";


    public Account()
    {

    }

    public Account(int account_id)
    {

    }

    public int generateNumber()
    {
        Random r = new Random();
        long accountNumber = (long)(r.nextDouble() * 900000000000l) + 100000000000l;

        Connection conn = DBManager.getConnection();

        try (
                PreparedStatement ps = conn.prepareStatement("select count(*) from account where account_number = ?")
        ) {
            ps.setLong(1, accountNumber);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Call PL/SQL function to calculate the current balance of the account
     * @return current account balance
     */
    public double getBalance()
    {
        // TODO implement method
        return -1;
    }

    public int save()
    {



        return -1;
    }

    public int delete()
    {
        return -1;
    }
}
