import dao.Account;
import java.util.Random;

public class Generate {
    public static void generatePerson()
    {
        Random r = new Random();

        int numAccount = r.nextInt(4);
    }

    public static void generateAccount()
    {
        Random r = new Random();

        long accountNumber = (long)(r.nextDouble() * 900000000000l) + 100000000000l;
        int accountType = r.nextInt(2);

        if (accountType == 0)
        {
            // checking
        }
        else
        {
            double interest_rate = r.nextInt(30) / 10.0;

            String sql = "insert into account values"
        }

    }
}
