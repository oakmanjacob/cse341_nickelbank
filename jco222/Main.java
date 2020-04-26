import dao.Branch;
import init.Init;
import util.DBManager;
import util.IOManager;
import view.Account_View;
import view.Bank_View;
import view.Purchase_View;

import java.util.Random;

public class Main {

    public static String[] slogans = {
            "Where your well-being is one of our priorities!",
            "Some say banks are a dime a dozen, we think they're 6 times more valuable than that!",
            "A penny rounded is a penny earned!",
            "Passwords stored in plain-text to maintain authenticity.",
            "Guaranteed to be less racist than the leading brands.",
            "Our loans may be subprime, but our customer service isn't!",
            "Built on a foundation of Liberty, Integrity, Equity and Security!"
    };

    public static void main(String[] args) {
        DBManager dbManager = new DBManager();
        dbManager.connect();

        // Print header ASCII art
        System.out.println("\n\n $$\\   $$\\ $$\\           $$\\                 $$\\       $$$$$$$\\                      $$\\       \n" +
                " $$$\\  $$ |\\__|          $$ |                $$ |      $$  __$$\\                     $$ |      \n" +
                " $$$$\\ $$ |$$\\  $$$$$$$\\ $$ |  $$\\  $$$$$$\\  $$ |      $$ |  $$ | $$$$$$\\  $$$$$$$\\  $$ |  $$\\ \n" +
                " $$ $$\\$$ |$$ |$$  _____|$$ | $$  |$$  __$$\\ $$ |      $$$$$$$\\ | \\____$$\\ $$  __$$\\ $$ | $$  |\n" +
                " $$ \\$$$$ |$$ |$$ /      $$$$$$  / $$$$$$$$ |$$ |      $$  __$$\\  $$$$$$$ |$$ |  $$ |$$$$$$  / \n" +
                " $$ |\\$$$ |$$ |$$ |      $$  _$$<  $$   ____|$$ |      $$ |  $$ |$$  __$$ |$$ |  $$ |$$  _$$<  \n" +
                " $$ | \\$$ |$$ |\\$$$$$$$\\ $$ | \\$$\\ \\$$$$$$$\\ $$ |      $$$$$$$  |\\$$$$$$$ |$$ |  $$ |$$ | \\$$\\ \n" +
                " \\__|  \\__|\\__| \\_______|\\__|  \\__| \\_______|\\__|      \\_______/  \\_______|\\__|  \\__|\\__|  \\__|");

        Random rand = new Random();
        System.out.println("\n\nWelcome to Nickel Bank, \"" + slogans[rand.nextInt(slogans.length)] + "\"");
        System.out.println("What can we help you with today?");

        Main.dashboard();

        dbManager.disconnect();
    }

    public static void dashboard()
    {
        while (true) {
            System.out.println("\nBanking Dashboard");
            System.out.println("(B)ank management, (A)ccounts, (P)urchases, (E)xit");

            String input = IOManager.getInputStringLower();

            switch (input) {
                case "b":
                case "bank":
                case "bank management":
                case "manage":
                case "management":
                    Bank_View.getView();
                    break;
                case "a":
                case "account":
                case "accounts":
                    Account_View.getView();
                    break;
                case "p":
                case "purchase":
                case "purchases":
                    Purchase_View.getView();
                    break;
                case "e":
                case "exit":
                case "quit":
                    System.out.println("We hate to see you go but love to watch you walk away.\n");
                    return;
                default: {
                    System.out.println("We do not have a section for \"" + input + "\" please try again");
                }
            }
        }
    }
}
