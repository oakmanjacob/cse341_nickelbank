package dao;

import util.DBManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {

    private int account_id;

    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private Date birth_date;

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
            ps.setDate(7, birth_date);

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



    // Getters and setters

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(Date birth_date) {
        this.birth_date = birth_date;
    }
}
