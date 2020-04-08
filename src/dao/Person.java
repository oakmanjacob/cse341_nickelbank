package dao;

import util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {

    private int account_id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String birth_date;

    private boolean connected;

    public boolean save()
    {
        Connection conn = DBManager.getConnection();
        String query = connected ? "replace" : "insert";
        query += "into person (first_name, last_name, email, phone, birth_date) values (?, ?, ?, ?, ?)";

        try (
                PreparedStatement ps = conn.prepareStatement(query);
        ) {
            //ps.setString(1, table_name);
            ps.setString(3, first_name);
            ps.setString(4, last_name);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setString(7, birth_date);

            ps.execute();
        }
        catch (SQLException e)
        {

        }

        return false;
    }

    public boolean delete()
    {
        return false;
    }
}
