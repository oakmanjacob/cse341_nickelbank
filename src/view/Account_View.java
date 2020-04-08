package view;

import dao.Person;
import util.IOManager;

public class Account_View {
    public static void getView()
    {
        System.out.println("Welcome to the accounts section, what would you like to do?");
        System.out.println("(W)ithdrawal, (D)eposit, (O)pen account, (B)ack");
        String input = IOManager.getInputStringLower();

        switch (input)
        {
            case "w":
            case "withdrawal":
                break;
            case "d":
            case "deposit":
                break;
            case "o":
            case "open account":
            case "new account":
            case "create account":
                Account_View.getOpenAccountView();
                break;
            default:
                System.out.println("");
                break;
        }
    }

    public static void getOpenAccountView()
    {
        System.out.println("Let's start the account creation process!");
        Person person;
        boolean section1 = false;

        while (!section1) {
            section1 = true;
            System.out.println("Have you banked with use before? (Y)es, (N)o");
            String input = IOManager.getInputStringLower();

            switch (input) {
                case "y":
                case "yes":
                    System.out.println("That's great, what is your name?");
                    person = Person_View.getFromName();
                    break;
                case "n":
                case "no":
                    System.out.println("We'll just need to ask a few questions about you then!");
                    person = Person_View.signup();
                    break;
                default:
                    section1 = false;
                    break;
            }
        }


        System.out.println("What type of account do you wish to create?");
    }
}
