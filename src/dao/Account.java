package dao;

import java.sql.*;
import util.DBManager;

public class Account implements DBObject {
    private static final String table_name = "account";



    public Account()
    {

    }

    public Account(int account_id)
    {

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
