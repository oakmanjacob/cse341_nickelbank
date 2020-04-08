package dao;

import util.DBManager;

import javax.xml.transform.Result;
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
                    "values (?, ?, ?, ?, ?)";
            try (
                    PreparedStatement ps = conn.prepareStatement(query);
            ) {
                //ps.setString(1, table_name);
                ps.setString(3, this.first_name);
                ps.setString(4, this.last_name);
                ps.setString(5, this.email);
                ps.setString(6, this.phone);
                ps.setDate(7, this.birth_date);

                if (ps.executeUpdate() == 0) {
                    return false;
                }
            } catch (SQLException e) {
                return false;
            }

            try (
                    PreparedStatement ps = conn.prepareStatement("SELECT person_id FROM person WHERE email = ?");
            ) {
                ps.setString(1, email);
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    return false;
                } else {
                    this.person_id = result.getLong("person_id");
                    this.connected = true;
                }
            } catch (SQLException e) {
                return false;
            }

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
