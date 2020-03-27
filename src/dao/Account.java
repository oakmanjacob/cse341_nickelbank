package dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import util.DBManager;

public class Account implements DBObject {
    private static final String table_name = "account";
    private Long account_id;
    private Long account_number;
    private StringBuilder type;
    private Double interest_rate;
    private Double min_balance;
    private Date created;

    private HashMap<String, Object> attributes;
    private HashSet<String> edited;


    public Account()
    {
        account_id = null;
        account_number = null;
        type = null;
        interest_rate = null;
        min_balance = null;

        attributes = new HashMap<String, Object>();
        edited = new HashSet<String>();

        attributes.put("account_id", account_id);
        attributes.put("account_number", account_number);
        attributes.put("type", type);
        attributes.put("interest_rate", interest_rate);
        attributes.put("min_balance", min_balance);
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

    public boolean save()
    {
        if (edited.size() != 0) {
            Connection conn = DBManager.getConnection();

            if (account_id == null)
            {
                StringBuilder query = new StringBuilder("insert into " + table_name + " (");
                String delim = "";
                Object[] edit_list = edited.toArray();

                for (Object s : edit_list) {
                    if (s instanceof String) {
                        query.append(delim);
                        query.append(s);
                        delim = ", ";
                    }
                }

                query.append(") values (");

                delim = "";

                for (Object s : edit_list) {
                    if (s instanceof String) {
                        query.append(delim);
                        query.append("?");
                        delim = ", ";
                    }
                }

                query.append(")");

                try (
                        PreparedStatement ps = conn.prepareStatement(query.toString());
                ) {
                    int i = 1;
                    for (Object s : edit_list) {
                        if (s instanceof String) {

                            Object attr = attributes.get(s);
                            if (attr instanceof Integer)
                            {
                                ps.setInt(i, (Integer)attr);
                            }
                            else if (attr instanceof Long)
                            {
                                ps.setLong(i, (Long)attr);
                            }
                            else if (attr instanceof Double)
                            {
                                ps.setDouble(i, (Double)attr);
                            }
                            else if (attr instanceof Date)
                            {
                                ps.setDate(i, (Date)attr);
                            }
                            else
                            {
                                ps.setString(i, attr.toString());
                            }
                            i++;
                        }
                    }

                    //System.out.println(query.toString());

                    ps.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else {
                StringBuilder query = new StringBuilder("update " + table_name + " set ");
                String delim = "";
                Object[] edit_list = edited.toArray();

                for (Object s : edit_list) {
                    if (s instanceof String) {
                        query.append(delim);
                        query.append(s);
                        query.append(" = ?");
                        delim = ", ";
                    }
                }

                query.append("where " + table_name + "_id = ?");

                try (
                    PreparedStatement ps = conn.prepareStatement(query.toString());
                ) {
                    int i = 1;
                    for (Object s : edit_list) {
                        if (s instanceof String) {

                            Object attr = attributes.get(s);
                            if (attr instanceof Integer)
                            {
                                ps.setInt(i, (Integer)attr);
                            }
                            else if (attr instanceof Long)
                            {
                                ps.setLong(i, (Long)attr);
                            }
                            else if (attr instanceof Date)
                            {
                                ps.setDate(i, (Date)attr);
                            }
                            else
                            {
                                ps.setString(i, attr.toString());
                            }
                            i++;
                        }
                    }
                    ps.setLong(i, this.account_id);
                    //ps.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            return true;
        }
        return false;
    }

    public boolean delete()
    {
        return false;
    }

    public void set(String attr, Object value)
    {
        this.attributes.replace(attr, value);
        this.edited.add(attr);
    }

    public Object get(String attr)
    {
        return this.attributes.get(attr);
    }
}
