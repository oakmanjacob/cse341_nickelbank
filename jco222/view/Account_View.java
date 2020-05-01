package view;

import dao.Account;
import dao.Branch;
import dao.Person;
import dao.Transaction;
import util.IOManager;

import java.text.SimpleDateFormat;
import java.util.List;

public class Account_View {
    public static Branch cur_branch = null;
    public static Account cache_account = null;
    public static Person cache_person = null;

    /**
     * Display the default interface for Account Management
     */
    public static void getView() {
        boolean repeat = true;
        while (repeat) {
            if (Account_View.cur_branch == null)
            {
                Account_View.cur_branch = Branch_View.getBranch();
            }

            System.out.println("\nAccount Management");
            if (Account_View.cur_branch != null)
            {
                System.out.println("From " + Account_View.cur_branch);
            }

            if (Account_View.cur_branch == null || "full".equals(Account_View.cur_branch.getType()))
            {

                repeat = Account_View.getBranchView();
            }
            else
            {
                repeat = Account_View.getATMView();
            }

        }
    }

    /**
     * Display the interface for branches with full functionality
     * @return whether to leave the Account Management interface
     */
    public static boolean getBranchView()
    {
        System.out.println("(A)ccount Info, (W)ithdrawal, (D)eposit, (C)reate account, (S)witch Branch, (B)ack");
        String input = IOManager.getInputStringLower();

        switch (input) {
            case "a":
            case "account":
            case "account info":
            case "info":
                Account_View.getAccountInfoView();
                break;
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
            case "s":
            case "switch":
            case "switch branch":
                Account_View.cur_branch = Branch_View.getBranch();
                Account_View.cache_person = null;
                Account_View.cache_account = null;
                break;
            case "b":
            case "back":
                return false;
            default :
                IOManager.printError();
                System.out.println("We do not have a section for \"" + input + "\". Please try again");
        }

        return true;
    }

    /**
     * Display the interface for ATMs with limited functionality
     * @return whether to leave the Account Management interface
     */
    public static boolean getATMView()
    {
        System.out.println("(A)ccount Info, (W)ithdrawal, (S)witch Branch, (B)ack");
        String input = IOManager.getInputStringLower();

        switch (input) {
            case "a":
            case "account":
            case "account info":
                Account_View.getAccountInfoView();
                break;
            case "w":
            case "withdrawal":
                Account_View.getWithdrawalView();
                break;
            case "s":
            case "switch":
            case "switch branch":
                Account_View.cur_branch = Branch_View.getBranch();
                Account_View.cache_person = null;
                Account_View.cache_account = null;
                break;
            case "b":
            case "back":
                return false;
        }

        return true;
    }

    /**
     * Display interface for getting information about a specific account
     */
    public static void getAccountInfoView() {
        Account account = getAccount();

        if (account == null || account.getAccountId() == 0)
        {
            return;
        }

        System.out.println("\nACCOUNT INFO");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Type: " + account.getType().toUpperCase());
        System.out.println("Balance: " + IOManager.formatCurrency(account.getBalance()));

        if ("checking".equals(account.getType()))
        {
            System.out.println("Minimum Balance: " + IOManager.formatCurrency(account.getMinBalance()));
        }
        else if ("savings".equals(account.getType()))
        {
            System.out.println("Interest Rate: " + account.getInterestRate() + "%");
        }

        if (account.getCreated() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateOpened = formatter.format(account.getCreated());
            System.out.println("Opened: " + dateOpened);
        }

        Account_View.cacheAccount(account);
    }

    /**
     * Walk the user through setting up a new withdrawal
     */
    public static void getWithdrawalView() {
        Account account = getAccount();

        if (account == null)
        {
            System.out.println("Canceling withdrawal process.");
            return;
        }

        boolean repeat = true;
        while (repeat) {
            System.out.println("How much would you like to withdrawal from your account?");
            System.out.println(account.toString());
            double amount = IOManager.getInputDouble(1);

            if (IOManager.yesNo("You are about to withdrawal " + IOManager.formatCurrency(amount) + " from " + account.toString() + ". Is this correct? (Y)es, (N)o")) {
                if (account.getBalance() - amount < 0)
                {
                    System.out.println("This withdrawal will overdraw your account and thus cannot be allowed");

                    if (IOManager.yesNo("Would you like to withdrawal a different amount from this account? (Y)es, (N)o")) {
                        repeat = true;
                        continue;
                    }
                    else
                    {
                        System.out.println("Canceling withdrawal process.");
                        repeat = false;
                    }

                    continue;
                }
                else if (account.getBalance() - amount < account.getMinBalance())
                {
                    System.out.println("This withdrawal will take your account below the minimum balance and may subject you to fees.");

                    if (!IOManager.yesNo("Are you sure you wish to continue? (Y)es, (N)o")) {
                        System.out.println("Canceling withdrawal process.");
                        repeat = false;
                        continue;
                    }
                }

                repeat = false;
                if (Account_View.cur_branch == null) {
                    Account_View.cur_branch = Branch_View.getBranch();
                }

                if (Account_View.cache_person == null) {
                    System.out.println("Please confirm your identity before withdrawal");
                    Account_View.cache_person = Person_View.getFromEmail(false);
                }

                if (account.hasOwner(Account_View.cache_person)) {
                    if (account.withdrawal(amount, cache_person, cur_branch)) {
                        System.out.println("Withdrawal successful!");
                        System.out.println("Your new balance is " + IOManager.formatCurrency(account.getBalance()));
                    } else {
                        System.out.println("There was an error with your withdrawal, please try again!");
                    }
                } else {
                    System.out.println("You do not currently have access to this account, aborting withdrawal");
                    Account_View.cache_person = null;
                    return;
                }
            }
            else {
                if (!IOManager.yesNo("Would you like to withdrawal a different amount from this account? (Y)es, (N)o")) {
                    System.out.println("Canceling withdrawal process.");
                    repeat = false;
                }
            }
        }

        Account_View.cache_person = null;
        Account_View.cacheAccount(account);
    }

    /**
     * Walk the user through setting up a new deposit
     */
    public static void getDepositView() {
        Account account = getAccount();

        if (account == null)
        {
            System.out.println("Canceling deposit process.");
            return;
        }

        boolean repeat = true;
        while (repeat) {
            System.out.println("How much would you like to deposit into your account?");
            System.out.println(account.toString());
            double amount = IOManager.getInputDouble(1);

            if (IOManager.yesNo("You are about to deposit " + IOManager.formatCurrency(amount) + " into " + account.toString() + ". Is this correct? (Y)es, (N)o")) {
                repeat = false;
                if (Account_View.cur_branch == null) {
                    Account_View.cur_branch = Branch_View.getBranch();
                }

                if (Account_View.cache_person == null) {
                    System.out.println("Please confirm your identity before deposit");
                    Account_View.cache_person = Person_View.getFromEmail(false);
                }

                if (account.hasOwner(Account_View.cache_person)) {
                    if (account.deposit(amount, cache_person, cur_branch)) {
                        System.out.println("Deposit successful!");
                        System.out.println("Your new balance is " + IOManager.formatCurrency(account.getBalance()));
                    } else {
                        System.out.println("There was an error with your deposit, please try again!");
                    }
                } else {
                    System.out.println("You do not currently have access to this account, aborting deposit");
                    return;
                }
            }
            else if (!IOManager.yesNo("Would you like to deposit a different amount into this account? (Y)es, (N)o")) {
                System.out.println("Canceling deposit process.");
                repeat = false;
            }
        }

        Account_View.cacheAccount(account);
    }

    /**
     * Walk the user through setting up a new account
     */
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

        cacheAccount(account);
    }

    /**
     * Walk user through creating a savings account
     * @return the created account or null if an account was not created
     */
    public static Account createSavingsAccount()
    {
        Account account = new Account("savings");

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

    /**
     * Walk user through creating a checking account
     * @return the created account or null if an account was not created
     */
    public static Account createCheckingAccount()
    {
        Account account = new Account("checking");

        boolean repeat = true;
        while (repeat)
        {
            repeat = false;
            System.out.println("At Nickel Bank we offer two types of checking accounts,\n" +
                    "(1) Recommended at a $100 minimum balance and\n" +
                    "(2) at the NOT Recommended $50 minimum balance\n" +
                    "Type 1 or 2 to select your preference or (E)xit to cancel account creation");
            String input = IOManager.getInputStringLower();

            switch (input)
            {
                case "1":
                    account.setMinBalance(100);
                    break;
                case "2":
                    account.setMinBalance(50);
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

    /**
     * Cache account upon request from user
     */
    public static void cacheAccount(Account account)
    {
        if (IOManager.yesNo("\nShould we keep this account handy for future transactions? (Y)es, (N)o")) {
            System.out.println("We'll keep it nearby then!");
            Account_View.cache_account = account;
        }
        else
        {
            Account_View.cache_person = null;
            Account_View.cache_account = null;
        }
    }

    /**
     * Help user identify an account they want to interact with
     * @return the account they wish to interact with or null if no account is selected
     */
    public static Account getAccount()
    {
        if (cache_account != null && IOManager.yesNo(
                "We have an account ending in " + cache_account.getLastFour() + " handy, is this the account you want? (Y)es, (N)o")) {
            return cache_account;
        }

        if (cur_branch != null && "atm".equals(cur_branch.getType()))
        {
            return getFromCard();
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

    /**
     * Walk user through looking up an account via an account number
     * @return the account or null if no account was found
     */
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

    /**
     * Walk user through looking up an account via a debit card
     * @return the account or null if no account was found
     */
    public static Account getFromCard()
    {
        System.out.println("Please input your debit card number.");

        long card_number = IOManager.getInputLong(1000000000000000l, 9999999999999999l);
        Account account = Account.fromDebitCard(card_number);

        if (account == null)
        {
            if (IOManager.yesNo("We can't find an account with the debit card number " + card_number + ".\n" +
                    "This number may be invalid or belong to a card which has been deactivated. Do you wish to try again? (Y)es, (N)o"))
            {
                return getFromCard();
            }

            return null;
        }

        Account_View.cache_person = Person.fromCardNumber(card_number);

        return account;
    }

    /**
     * Walk user through looking up an account via their email address
     * @return the account or null if no account was found
     */
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

        Account_View.cache_person = Person.fromEmail(email);

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

        Object object = IOManager.handleTable(account_list.toArray(), 5,false);

        if (object instanceof Account)
        {
            return (Account)object;
        }

        return null;
    }
}
