package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import util.DBManager;

public class Account {
    private static final String table_name = "account";

    public int account_id;
    public ArrayList<Integer> person_list;
    public double balance;
    public double min_balance;
    public String type;

    public Account(int account_id, double min_balance, String type)
    {
        this.account_id = account_id;
        this.min_balance = min_balance;
        this.type = type;

        balance = 0;
        person_list = new ArrayList<Integer>();
    }
}
