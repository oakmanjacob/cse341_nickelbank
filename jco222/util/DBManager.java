package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static Connection conn;

    public static Connection getConnection()
    {
        if (conn == null)
        {
            conn = login();
        }

        return conn;
    }

    public static void connect()
    {
        if (conn == null) {
            conn = login();
        }
    }

    public static void disconnect()
    {
        if (conn != null) {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean rollbackAndResetAutoCommit()
    {
        if (DBManager.conn == null)
        {
            return false;
        }

        try {
            DBManager.conn.rollback();
            DBManager.conn.setAutoCommit(true);
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection login() {
        Connection conn;
        String user;
        String password;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("enter Oracle user id: ");
            user = br.readLine();

            System.out.print("enter Oracle password for " + user + ": ");
            password = br.readLine();
        }
        catch (IOException e)
        {
            System.out.println("Input error, re-input connection data");
            return login();
        }

        try {
            System.out.println("Trying to connect with user " + user + " and password " + password);
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", user, password);
        }
        catch (SQLException e)
        {
            System.out.println(e);
            System.out.println("Connect error, re-input connection data");
            return login();
        }

        return conn;
    }
}
