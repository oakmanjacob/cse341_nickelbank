package util;

import view.Person_View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class IOManager {

    public static long getInputLong(long min, long max)
    {
        String input = IOManager.getInputString();

        try {
            long number = Long.parseLong(input);

            if (number < min || number >= max)
            {
                System.out.println("The number " + number + " is not valid, please try again!");
                return getInputLong(min, max);
            }

            return number;
        }
        catch (NumberFormatException e)
        {
            System.out.println("The number you entered is formatted incorrectly, please try again");
            return getInputLong(min, max);
        }
    }

    public static String getInputStringEmail() {
        String email = IOManager.getInputString();

        if (isValidEmail(email))
        {
            return email;
        }
        else {
            System.out.println("The email entered is not valid, it should follow the form example@example.com");
            System.out.println("Please try again");
            return IOManager.getInputStringEmail();
        }
    }

    // isValid code found on geeksforgeeks.org
    // https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static String getInputStringPhone()
    {
        String phone = getInputString().replaceAll("[^0-9]", "");

        if (phone.length() <= 15 && phone.length() >= 10)
        {
            return phone;
        }
        else
        {
            System.out.println("It appears the phone number you entered is not valid. Remember to include your area code and try again");
            return IOManager.getInputStringPhone();
        }
    }

    public static Date getInputBirthDate()
    {
        Date date = getInputDate();
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = new Date(cal.getTimeInMillis());
        cal.add(Calendar.YEAR, -200);
        Date old = new Date(cal.getTimeInMillis());

        if (date.equals(yesterday))
        {
            System.out.println("Look, we weren't born yesterday, and neither were you.");
            System.out.println("Re-input your birth date of the form MM/DD/YYYY.");
            return IOManager.getInputBirthDate();
        }
        else if (date.before(yesterday) && date.after(old))
        {
            return date;
        }
        else
        {
            System.out.println("Invalid birth date, re-input your birth date of the form YYYY-MM-DD.");
            return IOManager.getInputBirthDate();
        }
    }

    public static Date getInputDate()
    {
        String input = getInputString();

        try {
            Date date = Date.valueOf(input);

            if (date != null)
            {
                return date;
            }
            else
            {
                System.out.println("Invalid birth date, re-input your birth date of the form YYYY-MM-DD.");
                return IOManager.getInputDate();
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Invalid birth date, re-input your birth date of the form YYYY-MM-DD.");
            return IOManager.getInputDate();
        }
    }

    public static String getInputStringLower() {
        return  IOManager.getInputString().toLowerCase();
    }

    public static String getInputString() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            return br.readLine();
        } catch (IOException e) {
            System.out.println("Something went wrong with your input, please try again!");
            return IOManager.getInputString();
        }
    }

    public static boolean yesNo(String message)
    {
        System.out.println(message);
        String input = IOManager.getInputStringLower();

        switch (input) {
            case "y":
            case "yes":
                return true;
            case "n":
            case "no":
                return false;
            default:
                return yesNo(message);
        }
    }

    public static char yesNoCancel(String message)
    {
        System.out.println(message);
        String input = IOManager.getInputStringLower();

        switch (input) {
            case "n":
            case "no":
                return 'n';
            case "y":
            case "yes":
                return 'y';
            case "c":
            case "cancel":
                return 'c';
            default:
                return yesNoCancel(message);
        }
    }
}
