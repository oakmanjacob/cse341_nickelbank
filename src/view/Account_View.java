package view;

import dao.Account;
import dao.Person;
import util.IOManager;

public class Account_View {
    public static void getView() {
        while (true) {
            System.out.println("Account Management");
            System.out.println("(W)ithdrawal, (D)eposit, (C)reate account, (B)ack");
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
                case "c":
                case "create account":
                    Account_View.getOpenAccountView();
                    break;
                case "b":
                case "back":
                    return;
            }
        }
    }

    public static void getOpenAccountView() {
        System.out.println("Let's start the account creation process!");
        Person person = null;

        if (IOManager.yesNo("Have you banked with use before? (Y)es, (N)o")) {
            System.out.println("That's great, what is your email?");
            person = Person_View.getFromEmail();
        } else {
            System.out.println("We'll just need to ask a few questions about you then!");
            person = Person_View.signup();
        }

        if (person == null) {
            System.out.println("Cancelling Account creation process.");
            return;
        }

        Account account = null;
        String account_type = null;
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            System.out.println("What type of account do you wish to create? (S)avings, (C)hecking, (E)xit");
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
                case "e":
                case "exit":
                    System.out.println("Exiting Account creation process.");
                    return;
                default:
                    repeat = true;
            }

            if (account == null)
            {
                System.out.println("Cancelling Account creation process.");
                return;
            }

            account.addPerson(person);

            if (IOManager.yesNo("Do you want us to also open several other accounts under your name without your knowledge? (Y)es, (Y)es"))
            {
                System.out.println("Perfect, we'll keep that in mind for later!");
            }
            else
            {
                System.out.println("Thanks for your input!");
            }

            if (!account.save())
            {
                System.out.println("There was some error in the account creation process!");
            }

            System.out.println("Account successfully created! your new account number is " +  account.getAccountNumber() + "\n\n");
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
            System.out.println("At Nickel Bank we offer two types of savings accounts,\n" +
                    "(1) Recommended at a 0.01% interest and\n" +
                    "(2) at the NOT Recommended 2% interest\n" +
                    "Type 1 or 2 to select your preference or (E)xit to cancel account creation");
            String input = IOManager.getInputStringLower();

            switch (input)
            {
                case "1":
                    account.setInterestRate(0.01);
                    break;
                case "2":
                    account.setInterestRate(2);
                    break;
                case "e":
                case "exit":
                    return null;
                default:
                    repeat = true;
            }
        }

        return account;
    }

    public static Account createCheckingAccount()
    {
        Account account = new Account();
        account.setType("checking");
        boolean repeat = true;

        while (repeat)
        {
            repeat = false;
            System.out.println("At Nickel Bank we offer two types of checking accounts,\n" +
                    "(1) Recommended at a 0.01% interest and\n" +
                    "(2) at the NOT Recommended 2% interest\n" +
                    "Type 1 or 2 to select your preference or (E)xit to cancel account creation");
            String input = IOManager.getInputStringLower();

            switch (input)
            {
                case "1":
                    account.setMinBalance(50);
                    break;
                case "2":
                    account.setMinBalance(100);
                    break;
                case "e":
                case "exit":
                    return null;
                default:
                    repeat = true;
            }
        }

        return account;
    }

    public static void getDepositView() {

    }

    public static void getWithdrawalView() {

    }
}
