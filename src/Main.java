import init.Init;
import util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        dbManager.connect();



        dbManager.disconnect();
    }
}
