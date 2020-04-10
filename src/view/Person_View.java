package view;

import dao.Person;
import util.DBManager;
import util.IOManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.sql.*;

public class Person_View {
    public static Person signup()
    {
        Person person = new Person();

        System.out.println("Please input your email address.");
        person.setEmail(IOManager.getInputStringEmail());
        System.out.println("Please input your first name.");
        person.setFirstName(IOManager.getInputString());
        System.out.println("Please input your last name.");
        person.setLastName(IOManager.getInputString());
        System.out.println("Please input your phone number");
        person.setPhone(IOManager.getInputStringPhone());
        System.out.println("Please input your birth data in the form YYYY-MM-DD");
        person.setBirthDate(IOManager.getInputBirthDate());

        if (person.save())
        {
            return person;
        }
        else
        {
            System.out.println("Something went wrong, the email entered may have already been used for a different account!");
            if (IOManager.yesNo("Do you wish to try again? (Y)es, (N)o"))
            {
                return Person_View.signup();
            }
            else {
                return null;
            }
        }
    }

    public static Person getFromName()
    {
        Connection conn = DBManager.getConnection();
        System.out.println("Please input your full name.");

        String name = IOManager.getInputString();

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT\n" +
                    "    person_id, first_name, last_name, birth_date\n" +
                    "FROM\n" +
                    "    person\n" +
                    "WHERE\n" +
                    "    LOWER(CONCAT(first_name, ' ', last_name) LIKE LOWER(?)" +
                    "LIMIT 10");

            ps.setString(1, name);

            ResultSet result = ps.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Person getFromEmail()
    {
        return null;
    }
}


