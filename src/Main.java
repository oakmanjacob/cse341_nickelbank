import java.io.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        Connection conn = login();

        try {
            conn.close();
        }
        catch(SQLException e)
        {
            // Do nothing
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
