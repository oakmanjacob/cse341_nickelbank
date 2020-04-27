package dao;

import oracle.jdbc.OraclePreparedStatement;
import util.DBManager;
import util.IOManager;

import java.sql.*;

public class Person {

    private long person_id = 0;

    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private Date birth_date;

    public Person()
    {

    }

    public Person(long person_id, String first_name, String last_name, String email, String phone, Date birth_date)
    {
        this.person_id = person_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birth_date = birth_date;
    }

    public boolean save()
    {
        Connection conn = DBManager.getConnection();

        if (this.person_id == 0) {
            String query = "insert into person" +
                    "(first_name, last_name, email, phone, birth_date)" +
                    "values (?, ?, ?, ?, ?) returning person_id into ?";
            try (
                    OraclePreparedStatement ps = (OraclePreparedStatement)conn.prepareStatement(query);
            ) {
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
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public static Person fromEmail(String email)
    {
        Connection conn = DBManager.getConnection();
        Person person = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
            "SELECT\n" +
                    "    person_id, first_name, last_name, email, phone, birth_date\n" +
                    "FROM\n" +
                    "    person\n" +
                    "WHERE\n" +
                    "    LOWER(email) = LOWER(?)");

            ps.setString(1, email);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                person = new Person(
                    result.getLong("person_id"),
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("email"),
                    result.getString("phone"),
                    result.getDate("birth_date"));
            }
            else {
                return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return person;
    }

    public boolean equals(Person other)
    {
        return this.person_id == other.person_id;
    }

    public boolean delete()
    {
        return false;
    }

    // Getters and setters

    public long getPersonId() { return this.person_id; }

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
