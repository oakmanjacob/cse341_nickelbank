package init;

import java.io.*;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import util.DBManager;

public class Init {

    // Tables in order of creation
    static String[] table_list = new String[]{
            "person",
            "address",
            "property",
            "account",
            "person_account",
            "loan",
            "loan_secured",
            "branch",
            "card",
            "card_credit",
            "card_debit",
            "transaction",
            "transaction_transfer",
            "transaction_payment_loan",
            "transaction_payment_credit",
            "vendor",
            "transaction_external"
    };

    public static void dropAllTable()
    {
        Connection conn = DBManager.getConnection();
        for (int i = table_list.length - 1; i >= 0; i--) {
            String table = table_list[i];
            try (
                Statement s = conn.createStatement();
                ) {
                    s.execute("drop table " + table);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initAllTable()
    {
        Connection conn = DBManager.getConnection();
        BufferedReader reader;
        StringBuilder sb;

        for (int i = 0; i < table_list.length; i++) {
            String table = table_list[i];
            try {
                reader = new BufferedReader(new FileReader( "src" + File.separator
                        + "init" + File.separator + "table" + File.separator + table + ".sql"));
                sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n ");
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }

            try (
                    Statement s = conn.createStatement();
            ) {
                s.execute(sb.toString());
            }
            catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static void dropGenNum()
    {
        Connection conn = DBManager.getConnection();

        try (
                Statement s = conn.createStatement();
        ) {
            s.execute("drop trigger account_num_gen");
            s.execute("drop trigger card_num_gen");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initGenNum()
    {
        Connection conn = DBManager.getConnection();

        try (
                Statement s = conn.createStatement();
        ) {
            s.execute("create or replace trigger account_num_gen\n" +
                    "before insert on account\n" +
                    "referencing new as n\n" +
                    "for each row\n" +
                    "declare\n" +
                    "account_num number;\n" +
                    "in_table number := 1;\n" +
                    "begin\n" +
                    "\tif :n.account_number is null then\n" +
                    "\t\twhile in_table != 0\n" +
                    "\t\tloop\n" +
                    "\t\t\tselect round(dbms_random.value(100000000000, 999999999999)) into account_num from dual;\n" +
                    "\t\t\tselect count(*) into in_table from account where account_number = account_num;\n" +
                    "\t\tend loop;\n" +
                    "\t\t:n.account_number := account_num;\n" +
                    "\tend if;\n" +
                    "end;");

            s.execute("create or replace trigger card_num_gen\n" +
                    "before insert on card\n" +
                    "referencing new as n\n" +
                    "for each row\n" +
                    "declare\n" +
                    "card_num number;\n" +
                    "in_table number := 1;\n" +
                    "begin\n" +
                    "\tif :n.card_number is null then\n" +
                    "\t\twhile in_table != 0\n" +
                    "\t\tloop\n" +
                    "\t\t\tselect round(dbms_random.value(1000000000000000, 9999999999999999)) into card_num from dual;\n" +
                    "\t\t\tselect count(*) into in_table from card where card_number = card_num;\n" +
                    "\t\tend loop;\n" +
                    "\t\t:n.card_number := card_num;\n" +
                    "\tend if;\n" +
                    "end;");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initAllData()
    {

    }
}
