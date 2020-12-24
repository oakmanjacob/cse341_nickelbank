package view;

import util.IOManager;

public class Bank_View {

    /**
     * Display the main Bank Management area
     */
    public static void getView()
    {
        System.out.println("Hey there banker, I bet you wanna see some fat stats about the health of your bank!");
        System.out.println("Just sit back, relax, and don't think too hard about whether you're a drain on society.");
        System.out.println("Which statistics would you like to see today?");
        System.out.println("(T)otal Deposits, (B)ranch Activity (E)xit");

        String input = IOManager.getInputStringLower();

        //TODO finish this
    }

    public static void getTotalBalance()
    {

    }

}
