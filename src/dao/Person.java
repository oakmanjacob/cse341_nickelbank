package dao;

import oracle.jdbc.OraclePreparedStatement;
import util.DBManager;
import java.sql.*;

public class Person {

    private long person_id;

    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private Date birth_date;

    private boolean connected;

    public boolean save()
    {
        Connection conn = DBManager.getConnection();

        if (!connected) {
            String query = "insert into person" +
                    "(first_name, last_name, email, phone, birth_date)" +
                    "values (?, ?, ?, ?, ?) returning person_id into ?";
            try (
                    OraclePreparedStatement ps = (OraclePreparedStatement)conn.prepareStatement(query);
            ) {
                conn.setAutoCommit(false);
                //ps.setString(1, table_name);
                ps.setString(1, this.first_name);
                ps.setString(2, this.last_name);
                ps.setString(3, this.email);
                ps.setString(4, this.phone);
                ps.setDate(5, this.birth_date);
                ps.registerReturnParameter(6, Types.NUMERIC);

                if (ps.executeUpdate() == 0) {
                    return false;
                }

                ResultSet rs = ps.getReturnResultSet();

                if (rs != null && rs.next()) {
                    this.person_id = rs.getLong(1);
                }
                else
                {
                    throw new SQLException("Row possibly not inserted or something");
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
