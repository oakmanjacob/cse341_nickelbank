import util.DBManager;

public class Main {

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        dbManager.connect();
        dbManager.disconnect();
    }
}
