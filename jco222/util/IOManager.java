package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class IOManager {

    public static long getInputLong(long min, long max)
    {
        String input = IOManager.getInputString();

        try {
            long number = Long.parseLong(input);

            if (number < min || number >= max)
            {
                System.out.println("The number " + input + " is not valid, please try again!");
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

    public static int getInputInt(int min, int max)
    {
        String input = IOManager.getInputString();

        try {
            int number = Integer.parseInt(input);

            if (number < min || number >= max)
            {
                System.out.println("The number " + input + " is not valid, please try again!");
                return getInputInt(min, max);
            }

            return number;
        }
        catch (NumberFormatException e)
        {
            System.out.println("The number you entered is formatted incorrectly, please try again");
            return getInputInt(min, max);
        }
    }

    public static double getInputDouble(double min, double max)
    {
        double number = getInputDouble(min);

        if (number > max)
        {
            System.out.println("The number " + number + " is not in the valid range, please try again!");
            return getInputDouble(min, max);
        }

        return number;
    }

    public static double getInputDouble(double min)
    {
        String input = IOManager.getInputString();

        try {
            double number = Double.parseDouble(input);

            if (number < min)
            {
                System.out.println("The number " + input + " is not in the valid range, please try again!");
                return getInputDouble(min);
            }

            return number;
        }
        catch (NumberFormatException e)
        {
            System.out.println("The number you entered is formatted incorrectly, please try again");
            return getInputDouble(min);
        }
    }

    public static Object handleTable(Object[] list, int page_limit, boolean disableSelect)
    {
        if (list.length == 0)
        {
            return null;
        }

        int page = 0;
        int total_pages = (int)Math.ceil((double)list.length / page_limit);
        boolean repeat = true;
        while (repeat) {
            for (int i = page_limit * page; i < list.length && i < page_limit * (page + 1); i++) {
                System.out.println("(" + (i + 1) + ") - " + list[i].toString());
            }

            int index = -1;

            if (list.length > page_limit)
            {
                System.out.println("Page " + (page + 1) + " of " + total_pages);

                if (disableSelect)
                {
                    System.out.println("Type (N)ext, (B)ack or (E)xit to navigate");
                }
                else
                {
                    System.out.println("Type (N)ext or (B)ack to navigate or type in a number to indicate your choice");

                }
                String input = IOManager.getPageNavInput(page, page_limit, list.length, disableSelect);

                if (input.equals("n"))
                {
                    page++;
                    continue;
                }
                else if (input.equals("b"))
                {
                    page--;
                    continue;
                }
                else if (input.equals("e"))
                {
                    return null;
                }
                else
                {
                    index = Integer.parseInt(input) - 1;
                }
            }
            else
            {
                System.out.println("Type in the number corresponding to which record you would like to choose");
                index = IOManager.getInputInt(1, list.length + 1) - 1;
            }

            if (index >= 0 && index < list.length)
            {
                return list[index];
            }
        }

        return null;
    }

    public static String getPageNavInput(int page, int page_limit, int size, boolean disableSelect)
    {
        String input = IOManager.getInputStringLower();
        int total_pages = (int)Math.ceil((double)size / page_limit);

        switch (input)
        {
            case "n":
            case "next":
                if (page + 1 < total_pages)
                {
                    return "n";
                }
                break;
            case "b":
            case "back":
                if (page > 0) {
                    return "b";
                }
                break;
            case "e":
            case "exit":
                if (disableSelect) {
                    return "e";
                }
        }

        if (disableSelect)
        {
            System.out.println("Your input is not valid, please try again.");
            return getPageNavInput(page, page_limit, size, true);
        }

        try {
            int index = Integer.parseInt(input);
            if (index > 0 && index <= size)
            {
                return "" + index;
            }

            System.out.println("The number you entered is out of the valid range, please try again.");
            return getPageNavInput(page, page_limit, size, false);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Your input is not valid, please try again.");
            return getPageNavInput(page, page_limit, size, false);
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

    public static String formatCurrency(double amount)
    {
        NumberFormat usd = NumberFormat.getCurrencyInstance(Locale.US);
        return usd.format(amount);
    }
}
