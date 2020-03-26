package init;

import java.sql.*;
import util.DBManager;

public class Init {

    static String[] autoinc_tables = new String[]{
            "person",
            "address",
            "account",
            "loan",
            "branch",
            "card",
            "transaction",
            "vendor"};

    public static void dropAllTable()
    {
        Connection conn = DBManager.getConnection();
        try (
            Statement s = conn.createStatement();
            ) {
            s.execute("drop table transaction_external");

            s.execute("drop table vendor");

            s.execute("drop table transaction_payment_credit");

            s.execute("drop table transaction_payment_loan");

            s.execute("drop table transaction_transfer");

            s.execute("drop table transaction");

            s.execute("drop table card_debit");

            s.execute("drop table card_credit");

            s.execute("drop table card");

            s.execute("drop table branch");

            s.execute("drop table loan_secured");

            s.execute("drop table loan");

            s.execute("drop table person_account");

            s.execute("drop table account");

            s.execute("drop table property");

            s.execute("drop table address");

            s.execute("drop table person");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initAllTable()
    {
        Connection conn = DBManager.getConnection();
        try (
                Statement s = conn.createStatement();
        ) {
            s.execute("create table person\n" +
                    "(person_id varchar(12),\n" +
                    "first_name varchar(64) not null,\n" +
                    "last_name varchar(64) not null,\n" +
                    "email varchar(255) not null,\n" +
                    "phone varchar(16) not null,\n" +
                    "birth_date date not null,\n" +
                    "created timestamp with time zone default current_timestamp not null,\n" +
                    "primary key (person_id))");

            s.execute("create table address\n" +
                    "(address_id varchar(12),\n" +
                    "line_1 varchar(255) not null,\n" +
                    "line_2 varchar(255),\n" +
                    "city varchar(128) not null,\n" +
                    "state varchar(16) not null,\n" +
                    "zip varchar(16) not null,\n" +
                    "primary key (address_id))");

            s.execute("create table property\n" +
                    "(person_id varchar(12),\n" +
                    "address_id varchar(12),\n" +
                    "primary key (person_id, address_id),\n" +
                    "foreign key (person_id) references person\n" +
                    "on delete cascade,\n" +
                    "foreign key (address_id) references address\n" +
                    "on delete cascade);");

            s.execute("create table account\n" +
                    "(account_id varchar(12),\n" +
                    "account_number varchar(20) not null,\n" +
                    "type varchar(10) default 'checking' not null,\n" +
                    "interest_rate numeric(5, 2) default 0 not null,\n" +
                    "min_balance numeric(4, 0) default 0 not null,\n" +
                    "created timestamp with time zone default current_timestamp not null,\n" +
                    "primary key (account_id),\n" +
                    "unique (account_number));");

            s.execute("create table person_account\n" +
                    "(person_id varchar(12),\n" +
                    "account_id varchar(12),\n" +
                    "primary key (person_id, account_id),\n" +
                    "foreign key (person_id) references person,\n" +
                    "foreign key (account_id) references account);");

            s.execute("create table loan\n" +
                    "(loan_id varchar(12),\n" +
                    "person_id varchar(12),\n" +
                    "type varchar(10) default 'unsecured' not null,\n" +
                    "amount numeric(10,8) not null,\n" +
                    "interest_rate numeric(5,2) default 0 not null,\n" +
                    "monthly_payment numeric(10,8)default 0 not null,\n" +
                    "created timestamp with time zone default current_timestamp not null,\n" +
                    "primary key (loan_id),\n" +
                    "foreign key (person_id) references person);");

            s.execute("create table loan_secured\n" +
                    "(loan_id varchar(12),\n" +
                    "address_id varchar(12),\n" +
                    "primary key (loan_id),\n" +
                    "foreign key (loan_id) references loan\n" +
                    "on delete cascade,\n" +
                    "foreign key (address_id) references address\n" +
                    "on delete set null);");

            s.execute("create table branch\n" +
                    "(branch_id varchar(8),\n" +
                    "address_id varchar(12) not null,\n" +
                    "type varchar(8) default 'full' not null,\n" +
                    "primary key (branch_id),\n" +
                    "foreign key (address_id) references address);");

            s.execute("create table card\n" +
                    "(card_id varchar(12),\n" +
                    "person_id varchar(12) not null,\n" +
                    "type varchar(8) default 'debit' not null,\n" +
                    "card_number varchar(16) not null,\n" +
                    "cvc varchar(4) not null,\n" +
                    "status varchar(16) not null,\n" +
                    "created timestamp with time zone default current_timestamp not null,\n" +
                    "modified timestamp with time zone,\n" +
                    "primary key (card_id),\n" +
                    "foreign key (person_id) references person);");

            s.execute("create table card_credit\n" +
                    "(card_id varchar(12),\n" +
                    "credit_limit numeric(10,8) default 0 not null,\n" +
                    "interest_rate numeric(5,2) default 0 not null,\n" +
                    "primary key (card_id),\n" +
                    "foreign key (card_id) references card\n" +
                    "on delete cascade);");

            s.execute("create table card_debit\n" +
                    "(card_id varchar(12),\n" +
                    "account_id varchar(12),\n" +
                    "primary key (card_id),\n" +
                    "foreign key (card_id) references card\n" +
                    "on delete cascade,\n" +
                    "foreign key (account_id) references account\n" +
                    "on delete set null);");

            s.execute("create table transaction\n" +
                    "(transaction_id varchar(12),\n" +
                    "parent_transaction_id varchar(12),\n" +
                    "person_id varchar(12),\n" +
                    "branch_id varchar(12),\n" +
                    "amount numeric(10,8) not null,\n" +
                    "type varchar(16) not null,\n" +
                    "status varchar(16) default 'fulfilled' not null,\n" +
                    "created timestamp with time zone default current_timestamp not null,\n" +
                    "primary key (transaction_id),\n" +
                    "foreign key (parent_transaction_id) references transaction(transaction_id)\n" +
                    "on delete set null,\n" +
                    "foreign key (person_id) references person,\n" +
                    "foreign key (branch_id) references branch\n" +
                    "on delete set null);");

            s.execute("create table transaction_transfer\n" +
                    "(transaction_id varchar(12),\n" +
                    "from_account_id varchar(12),\n" +
                    "to_account_id varchar(12),\n" +
                    "primary key (transaction_id),\n" +
                    "foreign key (transaction_id) references transaction\n" +
                    "on delete cascade,\n" +
                    "foreign key (from_account_id) references account(account_id)\n" +
                    "on delete set null,\n" +
                    "foreign key (to_account_id) references account(account_id)\n" +
                    "on delete set null);");

            s.execute("create table transaction_payment_loan\n" +
                    "(transaction_id varchar(12),\n" +
                    "loan_id varchar(12) not null,\n" +
                    "primary key (transaction_id),\n" +
                    "foreign key (transaction_id) references transaction\n" +
                    "on delete cascade,\n" +
                    "foreign key (loan_id) references loan);");

            s.execute("create table transaction_payment_credit\n" +
                    "(transaction_id varchar(12),\n" +
                    "card_id varchar(12) not null,\n" +
                    "primary key (transaction_id),\n" +
                    "foreign key (transaction_id) references transaction\n" +
                    "on delete cascade,\n" +
                    "foreign key (card_id) references card_credit);");

            s.execute("create table vendor\n" +
                    "(vendor_id varchar(8),\n" +
                    "name varchar(64) not null,\n" +
                    "primary key (vendor_id));");

            s.execute("create table transaction_external\n" +
                    "(transaction_id varchar(12),\n" +
                    "card_id varchar(12) not null,\n" +
                    "vendor_id varchar(8) not null,\n" +
                    "primary key (transaction_id),\n" +
                    "foreign key (card_id) references card,\n" +
                    "foreign key (vendor_id) references vendor);");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropAutoInc()
    {
        Connection conn = DBManager.getConnection();

        try (
                Statement s = conn.createStatement();
        ) {
            for (String table : autoinc_tables)
            {
                s.execute("drop trigger " + table + "_autoinc");
                s.execute("drop sequence " + table + "_id_seq");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initAutoInc()
    {
        Connection conn = DBManager.getConnection();

        try (
                Statement s = conn.createStatement();
        ) {
            for (String table : autoinc_tables)
            {
                s.execute("create sequence " + table + "_id_seq");
                s.execute("create trigger " + table + "_autoinc\n" +
                        "before insert on " + table + "\n" +
                        "for each row\n" +
                        "begin\n" +
                        "\tselect " + table + "_id_seq.nextval\n" +
                        "\tinto :new." + table + "_id\n" +
                        "\tfrom dual;\n" +
                        "end;");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initAllData()
    {

    }
}
