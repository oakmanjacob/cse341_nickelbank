package view;

import util.IOManager;

public class Bank_View {
    public static void getView()
    {
        System.out.println("Hey there banker, I bet you wanna see some fat stats about the health of your bank!");
        System.out.println("Just sit back, relax, and don't think too hard about whether you're a drain on society.");
        System.out.println("Which statistics would you like to see today?");
        System.out.println("(L)oans, (A)ccounts, (C)ustomers, (T)ransactions, (B)ranches, (E)xit");

        String input = IOManager.getInputStringLower();


    }
}
