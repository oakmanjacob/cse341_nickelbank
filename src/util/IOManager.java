package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.regex.Pattern;

public class IOManager {

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
            System.out.println("It appears the phone number you entered is not valid. Remember to include your area code");
            return IOManager.getInputStringPhone();
        }
    }

    public static Date getInputDate()
    {
        String input = getInputString();
        Date date

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
}
