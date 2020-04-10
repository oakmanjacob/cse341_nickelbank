package view;

import dao.Account;
import dao.Person;
import util.IOManager;

public class Account_View {
    public static void getView() {
        System.out.println("Welcome to the accounts section, what would you like to do?");
        System.out.println("(W)ithdrawal, (D)eposit, (O)pen account, (B)ack");
        String input = IOManager.getInputStringLower();

        switch (input) {
            case "w":
            case "withdrawal":
                Account_View.getWithdrawalView();
                break;
            case "d":
            case "deposit":
                Account_View.getDepositView();
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

    public static void getOpenAccountView() {
        System.out.println("Let's start the account creation process!");
        Person person = null;

        if (IOManager.yesNo("Have you banked with use before? (Y)es, (N)o")) {
            System.out.println("That's great, what is your name?");
            person = Person_View.getFromName();
        } else {
            System.out.println("We'll just need to ask a few questions about you then!");
            person = Person_View.signup();
        }

        if (person == null) {
            return;
        }

        Account account = null;
        String account_type = null;
        boolean repeat = true;
        while (!repeat) {
            repeat = false;
            System.out.println("What type of account do you wish to create? (S)avings, (C)hecking");
            account_type = IOManager.getInputStringLower();

            switch (account_type) {
                case "s":
                case "saving":
                case "savings":
                    account = createSavingsAccount();
                    break;
                case "c":
                case "checking":
                    account = createCheckingAccount();
                    break;
                default:
                    repeat = true;
            }
        }
    }

    public static Account createSavingsAccount()
    {
        Account account = new Account();
        account.setType("savings");
        boolean repeat = true;

        while (repeat)
        {
            repeat = false;
            System.out.println("At Nickel Bank we offer two types of savings accounts, (1) Recommended at a 0.01% interest and (2) at the NOT Recommended 2% interest or (E)xit to cancel account creation");
            String input = IOManager.getInputString();

            switch (input)
            {
                case "1":
                    account.setInterestRate(0.01);
                    break;
                case "2":
                    account.setInterestRate(2);
                    break;
                default:
                    repeat = true;
            }
        }

        if (IOManager.yesNo("Do you want us to also open several other accounts under your name without your knowledge? (Y)es, (Y)es"))
        {
            System.out.println("Perfect, we'll keep that in mind for later!");
        }
        else
        {
            System.out.println("Thanks for your input!");
        }

        account.save();

        return account;
    }

    public static Account createCheckingAccount()
    {
        return null;
    }

    public static void getDepositView() {

    }

    public static void getWithdrawalView() {

    }
}
