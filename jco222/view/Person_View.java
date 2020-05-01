package view;

import dao.Person;
import util.DBManager;
import util.IOManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.sql.*;

public class Person_View {

    /**
     * Sign up a new customer by asking for email
     * @return
     */
    public static Person signup()
    {
        System.out.println("Please input your email address.");
        return signup(IOManager.getInputStringEmail());
    }

    /**
     * Given an email adress, walk customer through sign up process
     * @param email the customers unique email address
     * @return The newly created and saved Person object or null if failed
     */
    public static Person signup(String email)
    {
        Person person = new Person();

        person.setEmail(email);
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
            System.out.println("Something went wrong, the email entered may have already been used for a different person!");
            if (IOManager.yesNo("Do you wish to try again? (Y)es, (N)o"))
            {
                return Person_View.signup();
            }
            else {
                return null;
            }
        }
    }

    /**
     * Walk through the process of looking up or signing up a customer for our system
     * @return a newly created Person object which has been populated and is in the db
     */
    public static Person getFromEmail()
    {
        return getFromEmail(true);
    }

    /**
     * Walk through the process of looking up or signing up a customer for our system
     * @param canCreate determine whether signing up a new person is allowed from this screen
     * @return a newly created Person object which has been populated and is in the db
     */
    public static Person getFromEmail(boolean canCreate)
    {
        System.out.println("Please input your email.");

        String email = IOManager.getInputStringEmail();
        Person person = Person.fromEmail(email);

        if (person == null)
        {
            if (IOManager.yesNo("We can't find a person with the email address " + email + " Do you wish to try again? (Y)es, (N)o"))
            {
                return getFromEmail();
            }

            if (canCreate && IOManager.yesNo("Do you wish to instead sign up a new person with the email address " + email + "? (Y)es, (N)o"))
            {
                return signup(email);
            }

            return null;
        }

        return person;
    }
}


