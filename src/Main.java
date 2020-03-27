import dao.Account;
import dao.Card;
import dao.Loan;
import init.Init;
import util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        dbManager.connect();

        generateTransactions();

        dbManager.disconnect();
    }

    public static void generateTransactions()
    {
        Connection conn = DBManager.getConnection();
        ArrayList<Account> account_list = new ArrayList<Account>();
        ArrayList<Card> card_list = new ArrayList<Card>();
        ArrayList<Card> credit_card_list = new ArrayList<Card>();
        ArrayList<Loan> loan_list = new ArrayList<Loan>();

        // 2, 4, 5, 6
        // 1, 3

        try
        {
            Statement s = conn.createStatement();

            ResultSet result = s.executeQuery("select * from account natural join person_account order by account_id");

            Account next = null;
            if (!result.next()) System.out.println ("Empty result.");
            else {
                do {
                    if (next == null || next.account_id != result.getInt("account_id")) {
                        next = new Account(result.getInt("account_id"), result.getDouble("min_balance"), result.getString("type"));
                    }

                    next.person_list.add(result.getInt("person_id"));

                    account_list.add(next);
                } while (result.next());
            }

            s.close();
            s = conn.createStatement();

            result = s.executeQuery("select * from card c left join card_debit cd on c.card_id = cd.card_id left join card_credit cc on c.card_id = cc.card_id");

            if (!result.next()) System.out.println ("Empty result.");
            else {
                do {
                    Card card = new Card(result.getInt("card_id"), result.getInt("person_id"), result.getString("type"));


                    if (card.type.equals("credit"))
                    {
                        card.credit_limit = result.getDouble("credit_limit");
                        credit_card_list.add(card);
                    }
                    else
                    {
                        card.account_id = result.getInt("account_id");
                    }

                    card_list.add(card);
                } while (result.next());
            }

            s.close();
            s = conn.createStatement();

            result = s.executeQuery("select * from loan");

            if (!result.next()) System.out.println ("Empty result.");
            else {
                do {
                    loan_list.add(new Loan(result.getInt("loan_id"), result.getInt("person_id"), result.getDouble("amount")));
                } while (result.next());
            }

            s.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Random r = new Random();

        int transaction_id = 1;

        Calendar cal = new GregorianCalendar(105, 0, 1);

        for(int i = 0; i < 200; i++) {
            int type_id = transaction_id < 40 ? 0 : r.nextInt(6);
            String type;
            Account account;
            Loan loan;
            Card card;

            Timestamp datetime = new Timestamp(cal.getTimeInMillis());

            double amount;
            System.out.println(transaction_id);

            try {
                PreparedStatement ps = conn.prepareStatement("insert into transaction (transaction_id, person_id, branch_id, amount, type, created) values (?, ?, ?, ?, ?, ?)");

                switch (type_id) {
                    case 0:
                        type = "deposit";
                        account = account_list.get(r.nextInt(account_list.size()));
                        amount = Math.round(r.nextDouble() * 1000000) / 100;

                        ps.setInt(1, transaction_id);
                        ps.setInt(2, account.person_list.get(r.nextInt(account.person_list.size())));
                        ps.setInt(3, r.nextInt(6) + 1);
                        ps.setDouble(4, amount);
                        ps.setString(5, type);
                        ps.setTimestamp(6, datetime);

                        System.out.println("Call transaction");
                        ps.executeUpdate();
                        ps.close();

                        ps = conn.prepareStatement("insert into transaction_transfer (transaction_id, to_account_id) values (?, ?)");
                        ps.setInt(1, transaction_id);
                        ps.setInt(2, account.account_id);

                        System.out.println("Add extra");
                        ps.executeUpdate();
                        ps.close();

                        account.balance += amount;
                        transaction_id++;

                        break;
                    case 1:
                        type = "withdrawal";
                        account = account_list.get(r.nextInt(account_list.size()));
                        amount = Math.round(r.nextDouble() * (account.balance - account.min_balance) * 100) / 100;

                        ps.setInt(1, transaction_id);
                        ps.setInt(2, account.person_list.get(r.nextInt(account.person_list.size())));
                        ps.setInt(3, r.nextInt(6) + 1);
                        ps.setDouble(4, amount);
                        ps.setString(5, type);
                        ps.setTimestamp(6, datetime);
                        ps.executeUpdate();
                        ps.close();

                        ps = conn.prepareStatement("insert into transaction_transfer (transaction_id, from_account_id) values (?, ?)");
                        ps.setInt(1, transaction_id);
                        ps.setInt(2, account.account_id);
                        ps.executeUpdate();
                        ps.close();

                        transaction_id++;

                        account.balance -= amount;
                        break;
                    case 2:
                        type = "transfer";
                        account = account_list.get(r.nextInt(account_list.size()));
                        Account to_account = account_list.get(r.nextInt(account_list.size()));

                        if (to_account.account_id == account.account_id)
                        {
                            break;
                        }

                        amount = Math.round(r.nextDouble() * (account.balance - account.min_balance) * 100) / 100;

                        ps.setInt(1, transaction_id);
                        ps.setInt(2, account.person_list.get(r.nextInt(account.person_list.size())));
                        ps.setInt(3, r.nextInt(6) + 1);
                        ps.setDouble(4, amount);
                        ps.setString(5, type);
                        ps.setTimestamp(6, datetime);
                        ps.executeUpdate();
                        ps.close();

                        ps = conn.prepareStatement("insert into transaction_transfer (transaction_id, from_account_id, to_account_id) values (?, ?, ?)");
                        ps.setInt(1, transaction_id);
                        ps.setInt(2, account.account_id);
                        ps.setInt(3, to_account.account_id);
                        ps.executeUpdate();
                        ps.close();

                        account.balance -= amount;
                        to_account.balance += amount;
                        transaction_id++;
                        break;
                    case 3:
                        type = "loan_payment";
                        loan = loan_list.get(r.nextInt(loan_list.size()));

                        if (loan.amount > 0)
                        {
                            amount = Math.round(r.nextDouble() * (loan.amount) * 100) / 100;

                            ps.setInt(1, transaction_id);
                            ps.setInt(2, loan.person_id);
                            ps.setInt(3, r.nextInt(6) + 1);
                            ps.setDouble(4, amount);
                            ps.setString(5, type);
                            ps.setTimestamp(6, datetime);
                            ps.executeUpdate();
                            ps.close();

                            ps = conn.prepareStatement("insert into transaction_payment_loan (transaction_id, loan_id) values (?, ?)");
                            ps.setInt(1, transaction_id);
                            ps.setInt(2, loan.loan_id);
                            ps.executeUpdate();
                            ps.close();

                            transaction_id++;
                        }

                        break;
                    case 4:
                        type = "card_payment";
                        card = credit_card_list.get(r.nextInt(credit_card_list.size()));

                        if (card.balance > 0)
                        {
                            amount = Math.round(r.nextDouble() * (card.balance) * 100) / 100;

                            ps.setInt(1, transaction_id);
                            ps.setInt(2, card.person_id);
                            ps.setInt(3, r.nextInt(6) + 1);
                            ps.setDouble(4, amount);
                            ps.setString(5, type);
                            ps.setTimestamp(6, datetime);
                            ps.executeUpdate();
                            ps.close();

                            ps = conn.prepareStatement("insert into transaction_payment_card (transaction_id, card_id) values (?, ?)");
                            ps.setInt(1, transaction_id);
                            ps.setInt(2, card.card_id);
                            ps.executeUpdate();
                            ps.close();

                            transaction_id++;
                        }

                        break;
                    case 5:
                        type = "external";
                        int vendor_id = r.nextInt(15) + 1;
                        card = card_list.get(r.nextInt(card_list.size()));

                        if (card.type.equals("debit"))
                        {
                            account = account_list.get((int)(card.account_id - 1));
                            amount = Math.round(r.nextDouble() * (account.balance - account.min_balance) * 100) / 100;
                            account.balance -= amount;
                        }
                        else
                        {
                            amount = Math.round(r.nextDouble() * (card.credit_limit - card.balance) * 100) / 100;
                        }

                        ps.setInt(1, transaction_id);
                        ps.setInt(2, card.person_id);
                        ps.setInt(3, r.nextInt(6) + 1);
                        ps.setDouble(4, amount);
                        ps.setString(5, type);
                        ps.setTimestamp(6, datetime);
                        ps.executeUpdate();
                        ps.close();


                        ps = conn.prepareStatement("insert into transaction_external (transaction_id, card_id, vendor_id) values (?, ?, ?)");
                        ps.setInt(1, transaction_id);
                        ps.setInt(2, card.card_id);
                        ps.setInt(3, vendor_id);
                        ps.executeUpdate();
                        ps.close();

                        transaction_id++;
                        break;

                }

            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return;
            }

            cal.add(Calendar.DAY_OF_MONTH, r.nextInt(20));
        }
    }
}
