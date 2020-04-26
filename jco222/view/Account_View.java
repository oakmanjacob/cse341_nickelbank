package view;

import dao.Account;
import dao.Branch;
import dao.Person;
import dao.Transaction;
import util.IOManager;

import java.util.List;

public class Account_View {
    public static Account last_account = null;
    public static Branch cur_branch = null;
    public static Person cur_person = null;

    public static void getView() {
        while (true) {
            if (Account_View.cur_branch == null)
            {
                getBranch();
            }

            System.out.println("\nAccount Management");
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

    public static void getBranch()
    {
        System.out.println("Which branch are you banking at?");
        List<Branch> branch_list = Branch.getAllBranch();

        try {
            if (branch_list.size() == 0) {
                Account_View.cur_branch = null;
            }
            else if (branch_list.size() == 1)
            {
                Account_View.cur_branch = branch_list.get(0);
            }
            else {
                Object object = IOManager.handleTable(branch_list.toArray(), 5);
                if (object instanceof Branch) {
                    Account_View.cur_branch = (Branch) object;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return;
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

        cacheAccount(account);
    }

    public static void cacheAccount(Account account)
    {
        if (IOManager.yesNo("Should we keep this account handy for a bit? (Y)es, (N)o")) {
            System.out.println("We'll keep it nearby then!");
            last_account = account;
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
        Account account = getAccount();

        if (account == null)
        {
            System.out.println("Canceling deposit process.");
            return;
        }

        System.out.println("How much would you like to deposit into your account?");
        System.out.println(account.toString());
        double amount = IOManager.getInputDouble(1);

        if (IOManager.yesNo("You are about to deposit $" + amount + " into " + account.toString() + ". Is this correct? (Y)es, (N)o"))
        {
            // Deposit the money
        }

        Account_View.cacheAccount(account);
    }

    public static Account getAccount()
    {
        if (last_account != null && IOManager.yesNo(
                "We have an account ending in " + last_account.getLastFour() + " handy, is this the account you want? (Y)es, (N)o")) {
            return last_account;
        }

        System.out.println("Would you like to access your account via (A)ccount number, (D)ebit card or (L)ookup an account?");
        String input = IOManager.getInputStringLower();

        switch (input)
        {
            case "a":
            case "number":
            case "account number":
                return getFromNumber();
            case "d":
            case "debit":
            case "debit card":
                return getFromCard();
            case "l":
            case "lookup":
                return getFromLookup();
            default:
                return getAccount();
        }
    }

    public static Account getFromNumber()
    {
        System.out.println("Please input your account number.");

        long account_number = IOManager.getInputLong(100000000000l, 999999999999l);
        Account account = Account.fromNumber(account_number);

        if (account == null)
        {
            if (IOManager.yesNo("We can't find an account with the number " + account_number + " Do you wish to try again? (Y)es, (N)o"))
            {
                return getFromNumber();
            }

            return null;
        }

        return account;
    }

    public static Account getFromCard()
    {
        System.out.println("Please input your debit card number.");

        long card_number = IOManager.getInputLong(1000000000000000l, 9999999999999999l);
        Account account = Account.fromDebitCard(card_number);

        if (account == null)
        {
            if (IOManager.yesNo("We can't find an account with the debit card number " + card_number + " Do you wish to try again? (Y)es, (N)o"))
            {
                return getFromCard();
            }

            return null;
        }

        return account;
    }

    public static Account getFromLookup()
    {
        System.out.println("Input your email address and we can give you a list of you accounts.");
        String email = IOManager.getInputStringEmail();

        List<Account> account_list = Account.getAllFromEmail(email);

        if (account_list.size() == 0)
        {
            if (IOManager.yesNo("We didn't find any emails associated with that account. Would you like to try a different one? (Y)es, (N)o"))
            {
                return getFromLookup();
            }
            else
            {
                 return null;
            }
        }

        if (account_list.size() == 1)
        {
            System.out.println("We found one account associated with your email.");
            System.out.println(account_list.get(0).toString());
            if (IOManager.yesNo("Is this the account you are looking for? (Y)es, (N)o"))
            {
                return account_list.get(0);
            }
            else
            {
                if (IOManager.yesNo("Would you like to try a different email? (Y)es, (N)o"))
                {
                    return getFromLookup();
                }
                else
                {
                    return null;
                }
            }
        }

        Object object = IOManager.handleTable(account_list.toArray(), 5);

        if (object instanceof Account)
        {
            return (Account)object;
        }

        return null;
    }

    public static void getWithdrawalView() {
        Account account = getAccount();

        if (account == null)
        {
            System.out.println("Canceling deposit process.");
            return;
        }

        double amount = 0;
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            System.out.println("How much would you like to deposit into your account?");
            System.out.println(account.toString());
            amount = IOManager.getInputDouble(1, account.getBalance());

            if (amount > account.getBalance() - account.getMinBalance()) {
                if (!IOManager.yesNo("This withdrawal will take your account below its minimum balance and subject you to penalties, is this what you want? (Y)es, (N)o")) {
                    repeat = false;
                }
            }
        }

        if (IOManager.yesNo("You are about to withdrawal $" + amount + " from " + account.toString() + ". Is this correct? (Y)es, (N)o"))
        {
            // Deposit the money
        }

        Account_View.cacheAccount(account);
    }
}
